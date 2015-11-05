package com.stulsoft.yscdcatalogue.scala.service

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javafx.collections.FXCollections
import javafx.collections.ObservableList

import com.stulsoft.yscdcatalogue.data.SoftItemTree
import com.stulsoft.yscdcatalogue.data.SearchResult

/**
 * Finds items.
 *
 * @author Yuriy Stul
 *
 */
class Search(softItemTree: SoftItemTree, searchText: String) {
  val logName = { val c = getClass.getName; c.substring(0, c.lastIndexOf('.')) }
  val logger: Logger = LogManager.getLogger(logName)

  def find(): ObservableList[SearchResult] = {
    logger.debug("Staring searching for {}.", searchText)
    val results: ObservableList[SearchResult] = FXCollections.observableArrayList()
    // TODO
    return results
  }

}