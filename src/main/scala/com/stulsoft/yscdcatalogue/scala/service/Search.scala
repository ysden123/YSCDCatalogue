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
 *
 * @author Yuriy Stul
 *
 */
object Search {
  val logName = { val c = getClass.getName; c.substring(0, c.lastIndexOf('.')) }
  val logger: Logger = LogManager.getLogger(logName)

  /**
   * Finds items.
   *
   * @return the collection of the found items
   */

  /**
   * Finds items that contains specified search text in the full path, in the comments, on in the storage name.
   * @param softItemTree
   *            the three with Soft Items
   * @param searchText
   *            the search text
   * @return collection with items that contains specified search text in the full path, in the comments, on in the storage name.
   */
  def find(softItemTree: SoftItemTree, searchText: String): ObservableList[SearchResult] = {
    require(softItemTree != null, "softItemTree could not be null.")
    require(searchText != null && searchText.length > 0, "searchText could not be null or empty.")
    logger.debug("Staring searching for {}.", searchText)
    val results: ObservableList[SearchResult] = FXCollections.observableArrayList()
    find(searchText, results, softItemTree.getRoot)
    logger.debug("{} entries were found.", String.valueOf(results.size))
    return results
  }

  private def find(searchText: String, results: ObservableList[SearchResult], node: SoftItemNode): Unit = {
    if (node.getData.getType == SoftItemType.DISK) {
      logger.debug("Looking inside {}", node.getData.getName)
      try {
        val diskItemTree = DBManager.getInstance.getDiskItemTree(node.getData.getDiskId)
        val rootNode = diskItemTree.getRoot

        //@formatter:off
        findInDisk(searchText, results,
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

    node.getChildren.asScala.foreach { child => find(searchText, results, child) }
  }

  private def findInDisk(searchText: String, results: ObservableList[SearchResult], diskItemNode: DiskItemNode, categoryName: String, diskName: String, treeItem: TreeItem[SoftItem]): Unit = {
    //@formatter:off
    if (StringUtils.containsIgnoreCase(diskItemNode.getData.getFullPath, searchText)
      || StringUtils.containsIgnoreCase(diskItemNode.getData.getComment, searchText)
      || StringUtils.containsIgnoreCase(diskItemNode.getData.getStorageName, searchText)) {
      val result = new SearchResult(categoryName, diskName, diskItemNode.getData.getFullPath, treeItem)
      results.add(result)
    }
    //@formatter:on

    diskItemNode.getChildren.asScala.foreach { child => findInDisk(searchText, results, child, categoryName, diskName, treeItem) }
  }
}