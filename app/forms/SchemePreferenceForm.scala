/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package forms

import forms.Mappings._
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._

import scala.language.implicitConversions

object SchemePreferenceForm {

  val form = Form(
    mapping(
      "schemeNames" -> list(nonEmptyTrimmedText("scheme.required", 128)),
      "happyWithOrder" -> nonEmptyTrimmedText("schemeorderhappiness.required", 3)
        .verifying("schemeorderhappiness.required", str => { List("on", "off").contains(str.trim) })
    )(Data.apply)(Data.unapply)
  )

  case class Data(
    schemeNames: List[String],
    happyWithOrder: String
  )
}