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

package models.services

import com.google.inject.ImplementedBy
import com.mohiva.play.silhouette.api.services.IdentityService
import models.{ CachedData, SecurityUser, UniqueIdentifier }
import play.api.mvc.{ Request, RequestHeader }

import scala.concurrent.Future
import uk.gov.hmrc.http.HeaderCarrier

@ImplementedBy(classOf[UserCacheService])
trait UserService extends IdentityService[SecurityUser] {

  def save(user: CachedData)(implicit hc: HeaderCarrier): Future[CachedData]

  def refreshCachedUser(userId: UniqueIdentifier)(implicit hc: HeaderCarrier, request: RequestHeader): Future[CachedData]

}
