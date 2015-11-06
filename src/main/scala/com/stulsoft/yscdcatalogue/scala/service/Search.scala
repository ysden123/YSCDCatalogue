package com.stulsoft.yscdcatalogue.scala.service

import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem

import com.stulsoft.yscdcatalogue.data.DiskItemNode
import com.stulsoft.yscdcatalogue.data.DiskItemTree
import com.stulsoft.yscdcatalogue.data.SoftItem
import com.stulsoft.yscdcatalogue.data.SoftItemNode
import com.stulsoft.yscdcatalogue.data.SoftItemType
import com.stulsoft.yscdcatalogue.data.SoftItemTree
import com.stulsoft.yscdcatalogue.data.SearchResult
import com.stulsoft.yscdcatalogue.persistence.DBManager;

import scala.collection.JavaConverters._

/**
 * Finds items.
 * @param softItemTree
 *            the three with Soft Items
 * @param searchText
 *            the search text
 * @author Yuriy Stul
 *
 */
class Search(softItemTree: SoftItemTree, searchText: String) {
  val logName = { val c = getClass.getName; c.substring(0, c.lastIndexOf('.')) }
  val logger: Logger = LogManager.getLogger(logName)

  /**
   * Finds items.
   *
   * @return the collection of the found items
   */
  def find: ObservableList[SearchResult] = {
    logger.debug("Staring searching for {}.", searchText)
    val results: ObservableList[SearchResult] = FXCollections.observableArrayList()
    find(results, softItemTree.getRoot)
    logger.debug("{} entries were found.", String.valueOf(results.size))
    return results
  }

  private def find(results: ObservableList[SearchResult], node: SoftItemNode): Unit = {
    if (node.getData.getType == SoftItemType.DISK) {
      logger.debug("Looking inside {}", node.getData.getName)
      try {
        val diskItemTree = DBManager.getInstance.getDiskItemTree(node.getData.getDiskId)
        val rootNode = diskItemTree.getRoot

        //@formatter:off
        findInDisk(results,
          rootNode,
          node.getParent.getData.getName,
          rootNode.getData.getStorageName,
          node.getTreeItem);
        //@formatter:on
      } catch {
        case e: Exception => {
          logger.error("Failed getting disk for {}. Error: {}", node.getData.getName, e.getMessage, e)
        }
      }
    }

    node.getChildren.asScala.foreach { child => find(results, child) }
  }

  private def findInDisk(results: ObservableList[SearchResult], diskItemNode: DiskItemNode, categoryName: String, diskName: String, treeItem: TreeItem[SoftItem]): Unit = {
    //@formatter:off
    if (StringUtils.containsIgnoreCase(diskItemNode.getData.getFullPath, searchText)
      || StringUtils.containsIgnoreCase(diskItemNode.getData.getComment, searchText)
      || StringUtils.containsIgnoreCase(diskItemNode.getData.getStorageName, searchText)) {
      val result = new SearchResult(categoryName, diskName, diskItemNode.getData.getFullPath, treeItem)
      results.add(result)
    }
    //@formatter:on

    diskItemNode.getChildren.asScala.foreach { child => findInDisk(results, child, categoryName, diskName, treeItem) }
  }
}