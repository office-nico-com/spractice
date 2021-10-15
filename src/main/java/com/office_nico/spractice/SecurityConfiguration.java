package com.office_nico.spractice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/h2-console/**", "/webjars/**", "/font-awesome-4.7.0/**", "/jquery-loading-overlay-master/**");
	}

	/**
	 * HttpSecurityの設定
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository())
		.ignoringAntMatchers("/t06o1ny8/**")
		.ignoringAntMatchers("/t06o1ny8/logout");

		// http.csrf().disable();

		// 認可の設定
		http.authorizeRequests()
				// 認証無しでアクセスできるURLを設定
				.antMatchers("/t06o1ny8/login").permitAll()
				.antMatchers("/t06o1ny8/logout").permitAll()

				//				.antMatchers("/t06o1ny8/**").hasAnyRole("ROLE_ADMIN")
				// 全部許可
				// .antMatchers("/**").permitAll()

				// 上記以外は認証が必要にする設定
//				.anyRequest()
//				.authenticated()
				
				// 上記以外でadmin以下に認証が必要な設定にする
				.mvcMatchers("/t06o1ny8/**")
				.authenticated()

				/*
				 * .and() .exceptionHandling() // 権限がない場合のハンドラーの設定
				 * .authenticationEntryPoint((new DefaultAuthenticationEntryPoint("/login",
				 * "/login?timeout"))) // ログインしなかった場合のハンドラーの設定 .accessDeniedHandler(new
				 * DefaultAccessDeniedHandler())
				 */

				// ログイン設定
				.and().formLogin()
				// 認証処理のパスを設定
				.loginProcessingUrl("/t06o1ny8/login")
				// ログインフォームのパスを設定
				.loginPage("/t06o1ny8/login")

				// 認証成功時にリダイレクトするURLを設定
				// .defaultSuccessUrl("/admin/storages", true)
				.defaultSuccessUrl("/t06o1ny8/my", true)

				// 認証失敗時にforwardするURLを設定
				.failureUrl("/t06o1ny8/login?error").usernameParameter("email").passwordParameter("password")

				.and().rememberMe().key("hfidaejjhnafcvoijhuifha1237e40123")

				.and().logout().invalidateHttpSession(true).deleteCookies("JSESSIONID")
				// ログアウトのURL(spring securityが勝手に処理してくれる)
				// 重要！ リンクはPOST Only
				.logoutUrl("/t06o1ny8/logout")
				// ログアウト後のURL
				.logoutSuccessUrl("/t06o1ny8/login?logout");
	}

	/**
	 * 設定
	 */
	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * 
	 * // パスワードは平文でDBに登録する為、「NoOpPasswordEncoder」を設定する
	 * auth.userDetailsService(authService).passwordEncoder(new
	 * BCryptPasswordEncoder());
	 * 
	 * }
	 */

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
