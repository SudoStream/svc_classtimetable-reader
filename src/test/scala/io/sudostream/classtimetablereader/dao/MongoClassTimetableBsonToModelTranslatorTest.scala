package io.sudostream.classtimetablereader.dao

import io.sudostream.classtimetablereader.dao.mongo.MongoFindQueriesImpl
import org.scalatest.FunSuite

class MongoClassTimetableBsonToModelTranslatorTest extends FunSuite {
  test("Calling translateBsonToClassTimetable with empty Bson returns none") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assert(translator.translateMaybeBsonToMaybeClassTimetable(None) === None )
  }

}
