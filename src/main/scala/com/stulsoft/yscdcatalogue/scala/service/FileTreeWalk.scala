package com.stulsoft.yscdcatalogue.scala.service

import java.io.File
import java.io.IOException
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.util.Date
import java.util.UUID

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import com.stulsoft.yscdcatalogue.Utils
import com.stulsoft.yscdcatalogue.data.DiskItem
import com.stulsoft.yscdcatalogue.data.DiskItemNode
import com.stulsoft.yscdcatalogue.data.DiskItemTree
import com.stulsoft.yscdcatalogue.data.DiskItemType

import scala.collection.JavaConverters._

/**
 * @author Yuriy Stul
 *
 */
object FileTreeWalk {
  val logName = { val c = getClass.getName; c.substring(0, c.lastIndexOf('.')) }
  val logger: Logger = LogManager.getLogger(logName)

  /**
   * Builds a tree of the DiskItem elements.
   *
   * @param startFolder
   *            start folder
   * @return the tree of the DiskItem elements.
   * @throws IOException
   *             if an error was occurred during walking through file tree.
   */
  @throws[IOException]("If an error was occurred during walking through file tree")
  def buildFileTree(startFolder: String): DiskItemTree = {
    require(startFolder != null && startFolder.length > 0, "startFolder could not be null or empty.")
    val startPath: Path = Paths.get(startFolder)
    val t: DiskItemTree = new DiskItemTree(new DiskItem(startFolder, DiskItemType.DIRECTORY, null, null))
    
    Files.walk(startPath).toArray().sortBy {_.toString()}.foreach { x => {
          var item: DiskItem = null
          val p: Path = x.asInstanceOf[Path]
          if (p.toFile.isDirectory) {
            item = new DiskItem(p.toString(), DiskItemType.DIRECTORY, null, null)
          } else {
            try {
              val attr: BasicFileAttributes = Files.readAttributes(p, classOf[BasicFileAttributes])
              item = new DiskItem(p.toString, DiskItemType.FILE, attr.size, new Date(attr.creationTime.toMillis))
            } catch {
    
              case e: Exception => {
                logger.error("Failed getting file attribute for {}. Error: {}", String.valueOf(p), e.getMessage, e)
              }
            }
          }
    
          if (item != null) {
            val node: DiskItemNode = new DiskItemNode(item, null)
            if (t.findNode(node.getData) == null) {
              val parentItem: DiskItem  = new DiskItem(p.getParent.toString, DiskItemType.DIRECTORY, null, null)
              val parent: DiskItemNode = t.findNode(parentItem)
              if (parent != null) {
                parent.addChild(node)
              }
            }
          }
      }
    }

    t.getRoot.getData.setType(DiskItemType.DISK)
    t.getRoot.getData.setStorageName(Utils.getStorageName(new File(startFolder)))

    val id: String = UUID.randomUUID.toString
    t.setId(id)

    return t
  }
}