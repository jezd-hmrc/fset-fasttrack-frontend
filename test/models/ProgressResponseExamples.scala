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

package models

import java.util.UUID

import connectors.exchange._

object ProgressResponseExamples {
  val Initial = ProgressResponse(UniqueIdentifier(UUID.randomUUID().toString),
    personalDetails = false,
    hasSchemeLocations = false,
    hasSchemes = false,
    assistanceDetails = false,
    review = false,
    questionnaire = QuestionnaireProgressResponse(),
    submitted = false,
    withdrawn = false,
    OnlineTestProgressResponse(invited = false,
      started = false,
      completed = false,
      expired = false,
      awaitingReevaluation = false,
      failed = false,
      failedNotified = false,
      awaitingAllocation = false,
      awaitingAllocationNotified = false,
    allocationConfirmed = false,
      allocationUnconfirmed = false),
    failedToAttend = false,
    assessmentScores = AssessmentScores(),
    assessmentCentre = AssessmentCentre()
  )
  val InProgress = Initial.copy(personalDetails = true)
  val InPersonalDetails = Initial.copy(personalDetails = true)
  val InSchemePreferencesDetails = InPersonalDetails.copy(hasSchemes = true)
  val InAssistanceDetails = InSchemePreferencesDetails.copy(assistanceDetails = true)
  val InDiversityQuestionnaire = InAssistanceDetails.copy(questionnaire = QuestionnaireProgressResponse(diversityStarted = true,
    diversityCompleted = true, educationCompleted = false, occupationCompleted = false))
  val InParentalOccupationQuestionnaire = InAssistanceDetails.copy(questionnaire = QuestionnaireProgressResponse(diversityStarted = true,
    diversityCompleted = true, educationCompleted = true, occupationCompleted = true))
  val InReview = InParentalOccupationQuestionnaire.copy(review = true)
  val Submitted = InReview.copy(submitted = true)
  val WithdrawnAfterSubmitted = Submitted.copy(withdrawn = true)
}
