package com.office_nico.spractice.repository.stock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.Stock;

@Repository
public class StockDaoImpl implements StockDao<Stock> {

	private static final long serialVersionUID = 2560630337235223176L;

	@PersistenceContext
	private EntityManager entityManager = null;

}
