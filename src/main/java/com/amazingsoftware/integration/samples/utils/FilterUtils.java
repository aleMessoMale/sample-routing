package com.amazingsoftware.integration.samples.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * This class is responsible for filtering List, Map and other collections under
 * certain specific conditions defined in the relative method.
 * 
 * @author al.casula
 *
 */
@Component
public class FilterUtils {

	Logger logger = LoggerFactory.getLogger(FilterUtils.class);

	/**
	 * This method filters a given list for the provided indexes.
	 * 
	 * @param listToFilter
	 *            the list to filter
	 * @param initialIndex
	 *            inclusive
	 * @param lastIndex
	 *            excluded
	 * @return the filtered list
	 */
	public <T> List<T> filterArrayListForIndex(List<T> listToFilter, int initialIndex, int lastIndex) {
		/* last index is equal to size since last index is excluded */
		int lastIndexInListToFilter = listToFilter.size();

		if (initialIndex > lastIndex) {
			throw new IllegalArgumentException("Initial index cannot be greater than last index");
		}

		if (initialIndex < 0 || lastIndex < 0) {
			throw new IllegalArgumentException("Index cannot be negative.");
		}

		/*
		 * Querying indexes greater than the size of the list to filter, don't
		 * throw error, just return an empty array
		 */
		if (initialIndex > lastIndexInListToFilter) {
			return new ArrayList<T>();
		}

		if (initialIndex < lastIndexInListToFilter && lastIndex > lastIndexInListToFilter) {
			logger.info("Moving last index from {} to {}", lastIndex, lastIndexInListToFilter);
			lastIndex = lastIndexInListToFilter;
		}

		List<T> filteredList = IntStream.range(initialIndex, lastIndex).mapToObj(listToFilter::get)
				.collect(Collectors.toList());

		return filteredList;
	}

}
