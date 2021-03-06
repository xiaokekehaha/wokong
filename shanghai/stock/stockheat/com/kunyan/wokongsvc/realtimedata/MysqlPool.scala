/**
* Copyright @ 2015 ShanghaiKunyan. All rights reserved
* @author     : Sunsolo
* Email       : wukun@kunyan-inc.com
* Date        : 2016-05-19 16:52
* Description : 
*/
package com.kunyan.wokongsvc.realtimedata 

import com.jolbox.bonecp.BoneCP
import com.jolbox.bonecp.BoneCPConfig
import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException

import java.sql.Connection
import java.sql.SQLException

/**
  * Created by wukun on 2016/5/18
  * mysql句柄池
  */
class MysqlPool private(val xmlHandle: XmlHandle, val isStock: Boolean = true) extends Serializable with CustomLogger {

  try {
    //Class.forName(xmlHandle.getElem("mySql", "driver"))
  } catch {
    case e: Exception => {
      errorLog(fileInfo, e.getMessage + "[The JDBC driver exception]")
      System.exit(-1)
    }
  }

  lazy val config = createConfig

  lazy val connPool = new BoneCP(config)

  /**
    * 初始化连接池配置
    * @author wukun
    */
  def createConfig: BoneCPConfig = {

    val initConfig = new BoneCPConfig

    if(isStock == true) {
      initConfig.setJdbcUrl(xmlHandle.getElem("mySql", "urlstock"))
      initConfig.setUsername(xmlHandle.getElem("mySql", "userstock"))
      initConfig.setPassword(xmlHandle.getElem("mySql", "passwordstock"))
    } else {
      initConfig.setJdbcUrl(xmlHandle.getElem("mySql", "urltest"))
      initConfig.setUsername(xmlHandle.getElem("mySql", "usertest"))
      initConfig.setPassword(xmlHandle.getElem("mySql", "passwordtest"))
    }

    initConfig.setMinConnectionsPerPartition(Integer.parseInt(xmlHandle.getElem("mySql", "minconn")))
    initConfig.setMaxConnectionsPerPartition(Integer.parseInt(xmlHandle.getElem("mySql", "maxconn")))
    initConfig.setPartitionCount(Integer.parseInt(xmlHandle.getElem("mySql", "partition")))
    initConfig.setConnectionTimeoutInMs(Integer.parseInt(xmlHandle.getElem("mySql", "timeout")))
    initConfig.setConnectionTestStatement("select 1")
    initConfig.setIdleConnectionTestPeriodInMinutes(Integer.parseInt(xmlHandle.getElem("mySql", "connecttest")))

    initConfig
  }

  def setConfig(mix: Int, max: Int, testPeriod: Long) {
    config.setPartitionCount(1)
    config.setMinConnectionsPerPartition(mix)
    config.setMaxConnectionsPerPartition(max)
    config.setIdleConnectionTestPeriodInMinutes(3)
    config.setIdleMaxAgeInMinutes(3)
  }

  /**
    * 获取连接
    * @author wukun
    */
  def getConnect: Option[Connection] = {

    var connect: Option[Connection] = null

    try {
      connect = Some(connPool.getConnection)
    } catch {

      case e: Exception => {

        if(connect != null) {
          connect.get.close
        }
        connect = None
      }
    }

    connect
  }

  def close {
    connPool.shutdown
  }
}

/**
  * Created by wukun on 2016/5/18
  * MysqlPool伴生对象
  */
object MysqlPool extends Serializable {
  def apply(xmlHandle: XmlHandle, isStock: Boolean = true): MysqlPool = {
    new MysqlPool(xmlHandle, isStock)
  }
}

