/*
 * Copyright 2019 HM Revenue & Customs
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

package config

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64

import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.Mode.Mode
import play.api.{ Configuration, Play }
import play.api.Play.{ configuration, current }
import uk.gov.hmrc.play.config.ServicesConfig

case class EmailConfig(url: EmailUrl, templates: EmailTemplates)

case class EmailUrl(host: String, sendEmail: String)
case class EmailTemplates(registration: String)

case class UserManagementConfig(url: UserManagementUrl)
case class UserManagementUrl(host: String)

/** configuration for the fast tract backend */
case class FasttrackConfig(url: FasttrackUrl)
case class FasttrackUrl(host: String, base: String)

case class FasttrackFrontendConfig(blockNewAccountsDate: Option[LocalDateTime], blockApplicationsDate: Option[LocalDateTime])
case class AddressLookupConfig(url: String)

object FasttrackFrontendConfig {
  def read(blockNewAccountsDate: Option[String], blockApplicationsDate: Option[String]): FasttrackFrontendConfig = {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    def parseDate(dateStr: String): LocalDateTime = LocalDateTime.parse(dateStr, format)

    new FasttrackFrontendConfig(blockNewAccountsDate map parseDate, blockApplicationsDate map parseDate)
  }
}

trait AppConfig {
  val analyticsToken: String
  val analyticsHost: String
  val emailConfig: EmailConfig
  val userManagementConfig: UserManagementConfig
  val fasttrackConfig: FasttrackConfig
  val fasttrackFrontendConfig: FasttrackFrontendConfig
  val addressLookupConfig: AddressLookupConfig
}

object FrontendAppConfig extends AppConfig with ServicesConfig {

  override def mode: Mode = Play.current.mode
  override def runModeConfiguration: Configuration = Play.current.configuration

  private def loadConfig(key: String) = configuration.getString(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  val feedbackUrl = configuration.getString("feedback.url").getOrElse("")

  override lazy val analyticsToken = loadConfig("microservice.services.google-analytics.token")
  override lazy val analyticsHost = loadConfig("microservice.services.google-analytics.host")

  override lazy val emailConfig = configuration.underlying.as[EmailConfig]("microservice.services.email")

  override lazy val userManagementConfig = configuration.underlying.as[UserManagementConfig]("microservice.services.user-management")
  override lazy val fasttrackConfig = configuration.underlying.as[FasttrackConfig]("microservice.services.fasttrack")
  override val fasttrackFrontendConfig = FasttrackFrontendConfig.read(
    blockNewAccountsDate = configuration.getString("application.blockNewAccountsDate"),
    blockApplicationsDate = configuration.getString("application.blockApplicationsDate")
  )
  override lazy val addressLookupConfig = configuration.underlying.as[AddressLookupConfig]("microservice.services.address-lookup")

  // Whitelist Configuration
  private def whitelistConfig(key: String): Seq[String] = Some(
    new String(Base64.getDecoder().decode(Play.configuration.getString(key).getOrElse("")), "UTF-8")
  ).map(_.split(",")).getOrElse(Array.empty).toSeq

  lazy val whitelist = whitelistConfig("whitelist")
  lazy val whitelistExcluded = whitelistConfig("whitelistExcludedCalls")
}
