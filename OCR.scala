package com.dfzlocal.poc.p112

import net.sourceforge.tess4j.Tesseract
import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.sql.SparkSession

class ImageFile {
  def processImage(img: ImageInputStream) = {
    val iterator = ImageIO.getImageReaders(img)
    val imageReader = iterator.next()
    imageReader.setInput(img)
    val pages = imageReader.getNumImages(true)
    val resText = StringBuilder.newBuilder
    (0 until pages).foreach(page => {
      val tess = new Tesseract
      resText.append(tess.doOCR(imageReader.read(page)))})
    resText.toString()
  }
}

object OCR {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("OCR").
      set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").
      set("spark.kryo.classesToRegister", "org.apache.hadoop.hdfs.client.HdfsDataInputStream").
      setExecutorEnv("TESSDATA_PREFIX", "/usr/share/tesseract/tessdata/")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    //    val path = "datafiles/tif"
    val path = args(0)
    val input = spark.sparkContext.binaryFiles(path)
    val count: Int = input.count.toInt
    val files = input.partitionBy(new HashPartitioner(count))
    val files2 = files.map(file => (file._1.substring(file._1.lastIndexOf("/")+1,file._1.lastIndexOf(".")),file._2))
    val files3 = files2.map(file => {
      (file._1,ImageIO.createImageInputStream(file._2.open()))
    })

    val files4 = files3.map(file => {
      val image = new ImageFile
      (file._1,image.processImage(file._2))
    })
    files4.saveAsTextFile(args(1))
  }
}
