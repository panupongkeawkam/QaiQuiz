<template>
  <div class="container pb-4">
    <div
      class="row py-3 mb-5"
      style="
        position: sticky;
        top: 0;
        background-color: var(--background-color);
        z-index: 1;
      "
    >
      <div
        class="col-4 d-flex justify-content-start align-items-center logo"
        @click="navigateToHomePage()"
      >
        <h2 class="logo-text p-0 m-0 mt-1">?ai?uiz</h2>
      </div>
      <div class="col-8 justify-content-end align-items-center d-flex">
        <div class="navbar-icon-container" @click="showCommentModal = true">
          <span class="navbar-icon">
            <b-icon icon="chat" style="margin-bottom: 2px" />
          </span>
        </div>
      </div>
    </div>

    <div class="row d-flex align-items-center my-3">
      <div class="col-auto justify-content-start pe-0">
        <span
          class="badge me-3"
          :style="[
            {
              backgroundColor: category.background,
              color: category.foreground,
              fontSize: '14px',
            },
          ]"
        >{{ category.title }}</span>
      </div>
      <div v-if="questionLoading" class="col-auto px-0 d-flex align-items-end">
        <div class="component-loading p-2 rounded" style="width: 120px"></div>
      </div>
      <div v-else class="col-auto d-flex align-items-center d-flex ps-0">
        <h3 class="text-light m-0" style="font-weight: bold">{{ quiz.title }}</h3>
        <h5 v-if="isFinishedQuiz" class="m-0 ms-4" style="color: grey">Completed</h5>
        <h5 v-else class="ms-4 m-0" style="color: grey">
          {{ currentQuestionIndex + 1 }} /
          {{ questions.length }}
        </h5>
      </div>
    </div>

    <div class="row py-2 px-0">
      <div class="progress-container shadow" :style="[{ opacity: isFinishedQuiz ? '0' : '1' }]">
        <div
          class="progress-value"
          :style="[
            {
              width: questionLoading
                ? '0px'
                : `${((currentQuestionIndex + 1) / questions.length) * 100}%`,
              backgroundColor: category.background,
            },
          ]"
        ></div>
      </div>
    </div>

    <div class="row py-4 d-flex flex-row justify-content-center" style="height: 720px">
      <div class="col-9 p-0">
        <div class="col-12 h-100">
          <div v-if="isFinishedQuiz" class="row question shadow p-5 h-100">
            <div class="col-6 d-flex justify-content-start">
              <div v-if="resultLoading">
                <div
                  class="component-loading rounded-circle"
                  style="width: 360px; height: 360px; padding: 1.8rem"
                >
                  <div
                    class="rounded-circle h-100 w-100 d-flex flex-column justify-content-center align-items-center"
                    style="background-color: var(--element-color)"
                  >
                    <div class="component-loading p-3 w-50 rounded mb-3"></div>
                    <div class="component-loading p-2 w-25 rounded"></div>
                  </div>
                </div>
              </div>
              <div v-else>
                <progress-circle
                  :completed-steps="score"
                  :total-steps="questions.length"
                  :circle-color="'#262626'"
                  :start-color="'#f6d242'"
                  :stop-color="'#ff52e5'"
                  :diameter="360"
                  :circle-width="28"
                >
                  <h5 style="color: white; font-size: 3em">{{ score }} of {{ questions.length }}</h5>
                  <h5 style="color: grey; font-size: 1.2em">Questions</h5>
                </progress-circle>
              </div>
            </div>
            <div class="col-6 d-flex flex-column justify-content-between">
              <div>
                <h1
                  class="mb-5"
                  :style="{
                    color: resultLoading ? 'orange' : 'mediumseagreen',
                  }"
                >{{ resultLoading ? "Processing..." : "Completed" }}</h1>
                <div class="mb-4">
                  <h5 class="mb-2" style="color: grey">Quiz Name</h5>

                  <div v-if="resultLoading" class="component-loading p-2 my-3 w-50 rounded"></div>
                  <h3 v-else style="color: white">{{ quiz.title }}</h3>
                </div>
                <div class="mb-4">
                  <h5 class="mb-2" style="color: grey">Category</h5>
                  <div v-if="resultLoading" class="component-loading p-2 my-3 w-50 rounded"></div>
                  <h3 v-else style="color: white">{{ category.title }}</h3>
                </div>
                <div class="mb-4">
                  <h5 class="mb-2" style="color: grey">Time Spent</h5>
                  <div v-if="resultLoading" class="component-loading p-2 my-3 w-50 rounded"></div>
                  <h3 v-else style="color: white">{{ timeDifference(startDateTime) }}</h3>
                </div>
              </div>
              <div class="d-flex justify-content-end">
                <div
                  v-if="!resultLoading"
                  class="primary-button shadow"
                  :style="[
                    {
                      backgroundColor: category.background,
                      color: category.foreground,
                    },
                  ]"
                  @click="donePressedHandler()"
                >Done</div>
              </div>
            </div>
          </div>
          <div v-else class="question shadow p-5 h-100 d-flex flex-column justify-content-between">
            <div v-if="questionLoading">
              <div class="component-loading p-3 rounded w-100 mb-3"></div>
              <div class="component-loading p-3 rounded w-75"></div>
            </div>
            <h1 v-else class="text-light">{{ questions[currentQuestionIndex].title }}</h1>
            <div v-if="questionLoading" class="d-flex flex-row flex-wrap">
              <div v-for="(number, index) in 4" :key="index" class="col-6 p-2">
                <div class="col-12 choice p-4 pb-4 h-100">
                  <div class="component-loading p-2 rounded w-75 mb-2"></div>
                </div>
              </div>
            </div>
            <div v-else class="d-flex flex-row flex-wrap">
              <div
                v-for="(choice, index) in questions[currentQuestionIndex]
                  .choices"
                :key="index"
                class="col-6 p-2"
              >
                <div
                  class="col-12 choice p-4 pb-4 h-100"
                  @click="!answered ? respondHandler(index) : null"
                  :style="[{ borderColor: choiceColorState(index) }]"
                >
                  <h5>{{ choice }}</h5>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <transition name="fade">
      <div v-show="showCommentModal" class="modal-canvas">
        <div
          class="modal-box-large shadow d-flex flex-column justify-content-between"
          style="height: 80%"
        >
          <div class="row" style="height: 10%">
            <div class="col-9 p-2" style="overflow-x: hidden">
              <h3 style="color: white">
                Comments
                <span style="color: grey; font-size: 0.8em; margin-left: 8px">
                  {{
                  commentsLoading ? "" : comments.length
                  }}
                </span>
              </h3>
            </div>
            <div class="col-3 d-flex justify-content-end">
              <div class="navbar-icon-container" @click="showCommentModal = false">
                <span class="navbar-icon">
                  <b-icon icon="x" font-scale="1.5" />
                </span>
              </div>
            </div>
          </div>
          <div class="py-2 modal-scrollbar-container" style="height: 78%">
            <div v-if="commentsLoading">
              <div class="col-12 d-flex flex-row" v-for="(i, index) in 3" :key="index">
                <div class="d-flex justify-content-center align-items-start py-2">
                  <div
                    class="component-loading rounded-circle p-3"
                    style="height: 48px; width: 48px"
                  ></div>
                </div>
                <div class="ps-3 pe-5 py-2">
                  <div class="component-loading rounded p-2 my-2" style="width: 120px"></div>
                  <div class="component-loading rounded p-2" style="width: 320px"></div>
                </div>
              </div>
            </div>
            <div v-else-if="comments.length === 0" class="col-12 p-5 d-flex justify-content-center">
              <h4 style="color: grey">No comment</h4>
            </div>
            <div
              v-else
              v-for="comment in comments"
              :key="comment._id"
              class="col-12 d-flex flex-row"
            >
              <div class="d-flex justify-content-center align-items-start py-2">
                <img
                  :src="
                    'https://api.multiavatar.com/' + comment.username + '.svg'
                  "
                  height="48"
                  alt
                />
              </div>
              <div class="ps-3 pe-5 py-2">
                <div
                  style="
                    color: white;
                    font-size: 1em;
                    margin-bottom: 4px;
                    font-weight: bold;
                  "
                >
                  {{ comment.username }}
                  <span
                    style="color: grey; font-size: 0.9em; margin-left: 6px"
                  >{{ timeSince(comment.createDateTime) }}</span>
                </div>
                <div style="color: white; font-size: 1.1em">{{ comment.message }}</div>
              </div>
            </div>
          </div>
          <div class="row py-2" style="height: 12%">
            <div class="col-12 d-flex flex-row">
              <div class="d-flex justify-content-center align-items-start py-2">
                <img :src="'https://api.multiavatar.com/' + user.name + '.svg'" height="48" alt />
              </div>
              <div class="px-3 py-2 w-100">
                <input
                  type="text"
                  class="text-input-underline w-100"
                  v-model="commentMessage"
                  placeholder="Add a comment..."
                  @keypress="commentInputKeypressHandler"
                />
              </div>
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
import { ProgressCircle } from "vue-progress-circle";
import SERVICE_URL from "../assets/service-url.json";

export default {
  name: "QuizDetail",
  components: { ProgressCircle },
  data() {
    return {
      user: {},
      quizId: "",
      quiz: {},
      quizStore: {},
      questions: [],
      category: {},
      currentQuestionIndex: 0,
      selectedChoiceIndex: -1,
      answered: false,
      isFinishedQuiz: false,
      startDateTime: null,
      score: 0,
      showCommentModal: false,
      commentMessage: "",
      comments: [],
      questionLoading: true,
      resultLoading: true,
      commentsLoading: true,
      loading: false,
    };
  },
  methods: {
    async getQuizByQuizId() {
      await axios
        .patch(SERVICE_URL.QUIZ_SERVICE + "/quiz", {
          quizId: this.quizId,
        })
        .then((res) => {
          this.quiz = res.data;
        });
    },
    async getComments() {
      this.commentsLoading = true;
      await axios
        .patch(SERVICE_URL.COMMENT_SERVICE + "/comment", {
          quizId: this.quizId,
        })
        .then((res) => {
          this.comments = res.data.reverse();
          this.commentsLoading = false;
        });
    },
    navigateToHomePage() {
      this.$router.push({ name: "HomePage" });
    },
    respondHandler(selectedChoiceIndex) {
      this.answered = true;
      this.selectedChoiceIndex = selectedChoiceIndex;
      this.score +=
        selectedChoiceIndex ===
        this.questions[this.currentQuestionIndex].correct;

      setTimeout(() => {
        this.selectedChoiceIndex = -1;
        this.answered = false;

        if (this.currentQuestionIndex + 1 === this.questions.length) {
          this.finishedQuiz();
          return;
        }

        this.currentQuestionIndex++;
      }, 1500);
    },
    async finishedQuiz() {
      this.resultLoading = true;
      this.isFinishedQuiz = true;
      var user = JSON.parse(localStorage.getItem("user"));
      await axios
        .post(SERVICE_URL.RESULT_SERVICE + "/result/submit", {
          quizId: this.quiz._id,
          userId: user._id,
          quizName: this.quiz.title,
          category: this.quiz.category,
          score: this.score,
          fullScore: this.questions.length,
        })
        .then((res) => {
          this.resultLoading = false;
        });
    },
    choiceColorState(index) {
      if (!this.answered) {
        return null;
      }

      var color = "grey";

      if (index === this.questions[this.currentQuestionIndex].correct) {
        color = "mediumseagreen";
      }

      if (
        this.selectedChoiceIndex !==
          this.questions[this.currentQuestionIndex].correct &&
        index === this.selectedChoiceIndex
      ) {
        color = "tomato";
      }

      return color;
    },
    timeSince(date) {
      date = new Date(date);
      var seconds = Math.floor((new Date() - date) / 1000);

      var interval = seconds / 31536000;

      if (interval > 1) {
        return Math.floor(interval) + " years ago";
      }
      interval = seconds / 2592000;
      if (interval > 1) {
        return Math.floor(interval) + " months ago";
      }
      interval = seconds / 86400;
      if (interval > 1) {
        return Math.floor(interval) + " days ago";
      }
      interval = seconds / 3600;
      if (interval > 1) {
        return Math.floor(interval) + " hours ago";
      }
      interval = seconds / 60;
      if (interval > 1) {
        return Math.floor(interval) + " minutes ago";
      }
      if (seconds <= 0) {
        return "Now";
      }
      return Math.floor(seconds) + " seconds ago";
    },
    timeDifference(dateTime) {
      var date1 = new Date(dateTime);
      var date2 = new Date();

      var diff = date2.getTime() - date1.getTime();

      var msec = diff;
      var hh = Math.floor(msec / 1000 / 60 / 60);
      msec -= hh * 1000 * 60 * 60;
      var mm = Math.floor(msec / 1000 / 60);
      msec -= mm * 1000 * 60;
      var ss = Math.floor(msec / 1000);
      msec -= ss * 1000;

      return `${(mm + 100).toString().slice(1)}:${(ss + 100)
        .toString()
        .slice(1)}`;
    },
    donePressedHandler() {
      this.$router.push({
        name: "HomePage",
      });
    },
    async sendCommentHandler() {
      if (this.commentMessage.trim() === "") {
        return;
      }

      this.loading = true;
      axios
        .post(SERVICE_URL.COMMENT_SERVICE + "/comment", {
          quizId: this.quiz._id,
          userId: this.user._id,
          username: this.user.name,
          message: this.commentMessage.trim(),
        })
        .then((res) => {
          this.comments.unshift(res.data);
          this.commentMessage = "";
          this.loading = false;
        });
    },
    setBodyScrollable(enabled) {
      document.body.style.overflow = enabled ? "initial" : "hidden";
    },
    commentInputKeypressHandler(event) {
      if (event.key === "Enter") {
        this.sendCommentHandler();
      }
    },
  },
  watch: {
    showCommentModal(newState, oldState) {
      if (!oldState && newState) {
        this.getComments();
      }
      this.setBodyScrollable(!newState);
    },
  },
  async created() {
    this.quizId = this.$route.params.quizId;
    this.user = JSON.parse(localStorage.getItem("user"));

    this.questionLoading = true;
    if (this.$route.params.quiz === undefined) {
      await this.getQuizByQuizId();
    } else {
      this.quiz = this.$route.params.quiz;
    }

    this.questions = this.quiz.questions;
    this.category = this.$attrs.getCategoryByName(this.quiz.category);
    this.questionLoading = false;

    this.startDateTime = new Date();
  },
};
</script>
