<template>
  <div class="container">
    <div class="row py-3 mb-5">
      <div class="col-4 d-flex justify-content-start logo" @click="navigateToHomePage()">
        <h2 class="logo-text">?ai?uiz</h2>
      </div>
    </div>

    <div class="row mb-4">
      <div class="col-8 d-flex align-items-center justify-content-start">
        <span
          class="badge mb-2 me-3"
          :style="[
            {
              backgroundColor: category.background,
              color: category.foreground,
              fontSize: '14px',
            },
          ]"
        >{{ category.title }}</span>
        <div v-if="questionLoading" class="component-loading rounded p-2 mb-2" style="width: 120px"></div>
        <h3 v-else class="text-light" style="font-weight: bold">{{ quiz.title }}</h3>
        <h5 class="ms-4" style="color: grey">{{ questionLoading ? '' : questions.length }}</h5>
      </div>
      <div class="col-4 d-flex align-items-start justify-content-end">
        <div
          class="primary-button shadow text-light me-2"
          style="background-color: tomato"
          @click="showDeleteQuizModal = true"
        >
          <div class="p-0">
            <b-icon icon="trash" font-scale="1" style="box-sizing: border-box" />
          </div>
          <span class="me-1 ms-2" style="font-size: 0.9em; color: white">Delete</span>
        </div>
        <div
          class="primary-button shadow bg-light text-dark"
          @click="showCreateQuestionModal = true"
        >
          <b-icon icon="plus" font-scale="1.5" />
          <span class="me-1" style="font-size: 0.9em">Question</span>
        </div>
      </div>
    </div>

    <div v-if="questionLoading" class="row">
      <div class="col-6 p-3">
        <div class="question shadow p-4 h-100 d-flex flex-column justify-content-between">
          <div class="col-12 d-flex flex-wrap mb-5">
            <div class="col-8 p-2">
              <div class="component-loading rounded p-2 mb-3"></div>
              <div class="component-loading w-75 rounded p-2"></div>
            </div>
          </div>
          <div class="d-flex flex-row flex-wrap">
            <div v-for="(number, index) in 4" :key="index" class="col-6 p-2">
              <div
                class="col-12 p-3"
                style="
                  border-radius: 20px;
                  border: 1px grey solid
                "
              >
                <div class="component-loading w-75 rounded p-2 mb-3"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="questions.length === 0" class="row">
      <div class="col-12 p-5 my-5 d-flex justify-content-center">
        <h4 style="color: grey">No question</h4>
      </div>
    </div>

    <div v-else class="row mb-4">
      <div v-for="(question, index) in questions" :key="index" class="col-6 p-3">
        <div class="question shadow p-4 h-100 d-flex flex-column justify-content-between">
          <div class="col-12 d-flex flex-wrap">
            <div class="col-11">
              <h3 class="text-light mb-5 p-2">{{ index + 1 }}. {{ question.title }}</h3>
            </div>
            <div class="col-1">
              <div class="d-flex align-items-end flex-column">
                <div
                  class="navbar-icon-container"
                  @click="showDeleteQuestionModal = true;
                deletingQuestionIndex = index"
                >
                  <span class="navbar-icon">
                    <b-icon icon="x" font-scale="1.5" />
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="d-flex flex-row flex-wrap">
            <div v-for="(choice, index) in question.choices" :key="index" class="col-6 p-2">
              <div
                class="col-12 p-3"
                style="
                  border-radius: 20px;
                  border: 1px grey solid;
                  background-color: var(--element-color);
                  color: white;
                "
                :style="{
                  borderColor:
                    question.correct === index ? 'mediumseagreen' : 'grey',
                }"
              >
                <h6>{{ choice }}</h6>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <transition name="fade">
      <div v-show="showDeleteQuestionModal" class="modal-canvas">
        <div class="modal-box-small shadow d-flex flex-column" style="height: auto">
          <div class="row mb-4">
            <div class="col-12 p-2" style="overflow-x: hidden">
              <h3 style="color: white">Are you sure to delete this question?</h3>
            </div>
          </div>
          <div class="row">
            <div class="col-12 d-flex justify-content-end">
              <div
                class="primary-button"
                style="background-color: transparent"
                @click="showDeleteQuestionModal = false"
              >Cancel</div>
              <div
                class="primary-button shadow"
                style="background-color: tomato"
                @click="deleteQuestionHandler()"
              >Delete</div>
            </div>
          </div>
        </div>
      </div>
    </transition>
    <transition name="fade">
      <div v-show="showDeleteQuizModal" class="modal-canvas">
        <div class="modal-box-small shadow d-flex flex-column" style="height: auto">
          <div class="row mb-2">
            <div class="col-12 p-2" style="overflow-x: hidden">
              <h3 style="color: white">Are you sure to delete this quiz?</h3>
            </div>
          </div>
          <div class="row mt-3">
            <div class="col-6 d-flex align-items-center">
              <h6
                v-show="isInvalid"
                class
                style="color: tomato; font-style: italic"
              >{{ invalidMessage }}</h6>
            </div>
            <div class="col-6 d-flex align-items-center justify-content-end">
              <div
                class="modal-button"
                style="background-color: transparent"
                @click="closeDeleteQuizModal()"
              >Cancel</div>
              <div
                class="primary-button shadow text-light"
                style="background-color: tomato"
                @click="deleteQuizHandler()"
              >Delete</div>
            </div>
          </div>
        </div>
      </div>
    </transition>
    <transition name="fade">
      <div v-show="showCreateQuestionModal" class="modal-canvas">
        <div class="modal-box-large shadow d-flex flex-column" style="height: auto; width: 720px">
          <div class="row">
            <div class="col-9 p-2" style="overflow-x: hidden">
              <h3 style="color: white">Create Question</h3>
            </div>
            <div class="col-3 d-flex justify-content-end">
              <div class="navbar-icon-container" @click="closeCreateQuestionModal()">
                <span class="navbar-icon">
                  <b-icon icon="x" font-scale="1.5" />
                </span>
              </div>
            </div>
          </div>

          <div class="row mb-3">
            <div class="col-12 pt-2 pb-3 mb-5">
              <input
                type="text"
                placeholder="Question title"
                maxlength="60"
                v-model="newQuestionTitle"
                class="text-input-underline fs-5 w-100"
              />
            </div>
            <div class="col-12">
              <div class="row">
                <div v-for="(n, i) in 4" :key="n" class="col-6 p-2">
                  <div
                    class="col p-3 d-flex"
                    style="
                      border-radius: 20px;
                      border: 1px grey solid;
                      color: white;
                    "
                  >
                    <input
                      type="text"
                      class="text-input-underline p-2 w-75"
                      maxlength="120"
                      placeholder="Choice title"
                      v-model="newChoices[i]"
                    />
                    <div class="w-25 d-flex justify-content-end align-items-start">
                      <div class="checkbox-outer" @click="newCorrectChoiceIndex = i">
                        <div
                          class="checkbox-inner"
                          :style="{
                            transform: `scale(${
                              newCorrectChoiceIndex === i ? '1' : '0'
                            })`,
                          }"
                        ></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row py-2">
            <h6 v-show="isInvalid" class style="color: tomato">{{ invalidMessage }}</h6>
          </div>

          <div class="row mt-3">
            <div class="col-6 d-flex align-items-center">
              <div class="primary-button shadow colorful-bg" @click="generateQuestion()">
                <b-icon icon="dice3" class="me-2" />Random
              </div>
            </div>
            <div class="col-6 d-flex align-items-center justify-content-end">
              <div
                class="modal-button"
                style="background-color: transparent"
                @click="closeCreateQuestionModal()"
              >Cancel</div>
              <div
                class="primary-button shadow bg-white text-dark"
                @click="createQuestionHandler()"
              >Confirm</div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-show="loading" class="page-loader-canvas">
        <div class="loader" />
      </div>
    </transition>
  </div>
</template>

<script>
import axios from "axios";
import SERVICE_URL from "../assets/service-url.json";

export default {
  name: "QuizDetail",
  data() {
    return {
      quizId: "",
      questions: [],
      categoriesDict: {
        "general-knowledge": 9,
        film: 11,
        game: 15,
        "anime-and-manga": 31,
        sport: 21,
        computer: 18,
        history: 23,
        animal: 27,
        math: 19,
      },
      isSameName: false,
      quiz: {},
      category: "",
      showCreateQuestionModal: false,
      newQuestionTitle: "",
      newChoices: ["", "", "", ""],
      newCorrectChoiceIndex: -1,
      isInvalid: false,
      invalidMessage: "",
      choicesSet: new Set(),
      isChoiceEmpty: false,
      showDeleteQuizModal: false,
      showDeleteQuestionModal: false,
      deletingQuestionIndex: -1,
      questionLoading: false,
      loading: false,
    };
  },
  methods: {
    navigateToHomePage() {
      this.$router.push({ name: "HomePage" });
    },
    deleteQuestionHandler() {
      this.loading = true;
      setTimeout(() => {
        axios
          .delete(SERVICE_URL.QUIZ_SERVICE + "/quiz/question", {
            data: {
              quizId: this.quizId,
              questionIndex: this.deletingQuestionIndex,
            },
          })
          .then((res) => {
            if (res.data) {
              this.questions.splice(this.deletingQuestionIndex, 1);
              this.showDeleteQuestionModal = false;
              this.deletingQuestionIndex = -1;
              this.loading = false;
            }
          });
      }, 200);
    },
    deleteQuizHandler() {
      this.loading = true;
      setTimeout(() => {
        axios
          .delete(SERVICE_URL.QUIZ_SERVICE + "/quiz", {
            data: { quizId: this.quizId },
          })
          .then((res) => {
            if (res.data) {
              this.loading = false;
              this.showDeleteQuizModal = false;
              window.location.href = "/";
            }
          });
      }, 3000);
    },
    closeDeleteQuizModal() {
      this.showDeleteQuizModal = false;
      this.deletingQuestionIndex = -1;
    },
    closeCreateQuestionModal() {
      this.showCreateQuestionModal = false;
      this.newQuestionTitle = "";
      this.newChoices = ["", "", "", ""];
      this.newCorrectChoiceIndex = -1;
      this.isInvalid = false;
      this.invalidMessage = "";
    },
    shuffle(array) {
      let currentIndex = array.length,
        randomIndex;

      while (currentIndex != 0) {
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex--;

        [array[currentIndex], array[randomIndex]] = [
          array[randomIndex],
          array[currentIndex],
        ];
      }
      return array;
    },
    generateQuestion() {
      this.loading = true;
      axios
        .get(
          "https://opentdb.com/api.php?amount=1&category=" +
            this.categoriesDict[this.category.name] +
            "&type=multiple&difficulty=easy"
        )
        .then((res) => {
          this.newQuestionTitle = new DOMParser().parseFromString(
            res.data.results[0].question,
            "text/html"
          ).body.innerText;
          this.newChoices = res.data.results[0].incorrect_answers;
          var correctChoice = new DOMParser().parseFromString(
            res.data.results[0].correct_answer,
            "text/html"
          ).body.innerText;
          this.newChoices.push(correctChoice);

          this.newChoices = this.shuffle(this.newChoices).map(
            (newChoice) =>
              new DOMParser().parseFromString(newChoice, "text/html").body
                .innerText
          );
          this.newCorrectChoiceIndex = this.newChoices.findIndex(
            (choice) => choice === correctChoice
          );
          this.loading = false;
        });
    },
    createQuestionHandler() {
      this.isSameName = this.quiz.questions.some(
        (question) => question.title === this.newQuestionTitle
      );
      this.newChoices = this.newChoices.map((choice) => choice.trim());
      this.newQuestionTitle.trim();
      this.isChoiceEmpty =
        this.newChoices.filter((choice) => choice !== "").length !== 4;
      this.choicesSet = new Set(this.newChoices);
      this.newCorrectChoiceIndex;

      if (this.newQuestionTitle.trim() === "") {
        this.isInvalid = true;
        this.invalidMessage = "Invalid: Question title cannot be empty";
        return;
      }

      if (this.isSameName) {
        this.isInvalid = true;
        this.invalidMessage =
          "Invalid: Question title cannot be same as same quiz";
        return;
      }

      if (this.isChoiceEmpty) {
        this.isInvalid = true;
        this.invalidMessage = "Invalid: Choice title cannot be empty";
        return;
      }

      if (this.newCorrectChoiceIndex === -1) {
        this.isInvalid = true;
        this.invalidMessage = "Invalid: Please choose a correct choice";
        return;
      }

      if (this.choicesSet.size !== 4) {
        this.isInvalid = true;
        this.invalidMessage = "Invalid: Choice cannot be same as same question";
        return;
      }

      this.loading = true;
      axios
        .put(SERVICE_URL.QUIZ_SERVICE + "/quiz", {
          quizId: this.quizId,
          question: {
            title: this.newQuestionTitle,
            correct: this.newCorrectChoiceIndex,
            choices: this.newChoices,
          },
        })
        .then((res) => {
          this.questions = res.data.questions;
          this.loading = false;
          this.closeCreateQuestionModal();
        });
    },
    async fetchQuizById() {
      this.questionLoading = true;
      await axios
        .patch(SERVICE_URL.QUIZ_SERVICE + "/quiz", {
          quizId: this.quizId,
        })
        .then((res) => {
          this.quiz = res.data;
          this.questionLoading = false;
        });
    },
  },
  async created() {
    this.quizId = this.$route.params.quizId;

    if (this.$route.params.quiz === undefined) {
      await this.fetchQuizById();
    } else {
      this.quiz = this.$route.params.quiz;
    }

    this.questions = this.quiz.questions;
    this.category = this.$attrs.getCategoryByName(this.quiz.category);
  },
};
</script>
