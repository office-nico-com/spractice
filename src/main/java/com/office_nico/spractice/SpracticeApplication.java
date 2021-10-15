package com.office_nico.spractice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@PropertySources({
	@PropertySource(value="file:${application.config}", ignoreResourceNotFound=true, encoding = "utf-8"),
	@PropertySource(value="classpath:META-INF/build-info.properties", ignoreResourceNotFound=true)
})
public class SpracticeApplication {

	public static void main(String[] args) {
		
		try {

			// 引数でcatalina.outが指定されている場合は標準出力と標準エラー出力をファイルに向ける
			// → catalina.outの処理をプログラムの実装内で行う
			// → ローテートは
			String output = System.getProperty("catalina.out");
			if(output != null) {
				if(output != null) {
					FileOutputStream catalinaout = new FileOutputStream(new File(output),true);
					PrintStream out = new PrintStream(catalinaout);
					System.setOut(out);
					System.setErr(out);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SpringApplication.run(SpracticeApplication.class, args);
	}
}
