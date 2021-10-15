package com.office_nico.spractice.repository.stock;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Stock;


@Repository
@Transactional
public interface StockRepository extends JpaRepository<Stock,Long>, StockDao<Stock> {

	public List<Stock> findByScenarioIdOrderById(Long scenarioId);

	public Stock findTopByScenarioIdAndBinaryFileId(Long scenarioId, Long binaryFileId);

}
