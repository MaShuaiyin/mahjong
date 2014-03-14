package websiteschema.mpsegment.tools

import websiteschema.mpsegment.concept.Concept
import websiteschema.mpsegment.dict.IWord
import websiteschema.mpsegment.dict.POSUtil
import scala.util.parsing.json.{JSONArray, JSONObject}

class WordStringConverter(word: IWord) {

  def convertToString(): String = {
    var map = Map[String, Any]("word" -> word.getWordName())
    map += ("domainType" -> word.getDomainType())
    getPOSTable() match {
      case Some(posTable) => map += ("POSTable" -> posTable)
      case _ =>
    }
    getConcepts() match {
      case Some(concepts) => map += ("concepts" -> concepts)
      case _ =>
    }
    JSONObject(map).toString()
  }

  private def getPOSTable(): Option[JSONObject] = {
    var map = Map[String, Int]()
    val POSTable = word.getWordPOSTable()
    if (null != POSTable && POSTable.length > 0) {
      for (POSAndFreq <- POSTable) {
        map += (POSUtil.getPOSString(POSAndFreq(0)) -> POSAndFreq(1))
      }
      Some(JSONObject(map))
    } else {
      None
    }
  }

  private def getConcepts(): Option[JSONArray] = {
    val concepts = word.getConcepts()
    if (null != concepts && concepts.length > 0 && concepts(0) != Concept.UNKNOWN)
      Some(JSONArray(concepts.map(concept => concept.getName()).toList))
    else
      None
  }

}
