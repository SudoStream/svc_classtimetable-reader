package io.sudostream.classtimetablereader.dao

import org.scalatest.FunSuite

class MongoClassTimetableBsonToModelTranslatorTest extends FunSuite with MongoClassTimetableBsonToModelTranslatorTestHelper {

  test("Calling translateBsonToClassTimetable with empty Bson returns none") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assert(translator.translateMaybeBsonToMaybeClassTimetable(None) === None)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns defined") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(createValidClassTimetableBson)
    assert(maybeClassTimetable.isDefined)
  }


}
