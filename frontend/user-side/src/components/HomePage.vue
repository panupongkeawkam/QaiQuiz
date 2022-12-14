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
      <div class="col-4 d-flex justify-content-start align-items-center logo">
        <h2 class="logo-text p-0 m-0">?ai?uiz</h2>
      </div>
      <div class="col-8 justify-content-end align-items-center d-flex">
        <div class="search-box-container shadow d-flex flex-row">
          <span class="search-icon pe-3" @click="searchHandler()">
            <b-icon icon="search" variant="light" />
          </span>
          <input
            class="search-box"
            placeholder="Search Quiz"
            v-model="searchQuery"
            @keypress="searchBoxKeyPressHandler"
          />
        </div>
        <div class="navbar-icon-container" @click="showResponseModal = true">
          <span class="navbar-icon">
            <b-icon icon="clock" style="margin-bottom: 1px" />
          </span>
        </div>
        <div class="navbar-icon-container" @click="showProfileModal = true">
          <span class="navbar-icon" style="font-size: 28px">
            <b-icon icon="person" />
          </span>
        </div>
      </div>
    </div>

    <div class="row my-3">
      <div class="col d-flex align-items-start">
        <h1 class="text-light font-weight-bold">Categories</h1>
      </div>
    </div>

    <div class="row">
      <div class="col-12 pt-1 pb-3 d-flex flex-row horizontal-scroll">
        <div
          class="p-4 category"
          :class="{ 'me-3': index !== categories.length - 1 }"
          v-for="(category, index) in categories"
          :key="index"
          :style="[{ backgroundColor: category.background }]"
          @click="clickCategoryHandler(category)"
        >
          <span class="pe-3 category-background-icon">
            <b-icon :icon="category.iconName" variant="light" />
          </span>
          <div
            class="col-12 d-flex justify-content-start"
            :style="[{ color: category.foreground }]"
          >
            <h3 class="fs-1" style="text-align: left; font-weight: bold">
              {{ category.title }}
            </h3>
          </div>
        </div>
      </div>
    </div>

    <div class="row my-3">
      <div class="col d-flex align-items-start">
        <h1 class="text-light font-weight-bold">{{ quizLabelTitle }}</h1>
      </div>
    </div>

    <div class="row" v-if="quizLoading">
      <div class="col-4" v-for="(number, index) in 9" :key="index">
        <div class="quiz shadow p-4 pb-3 mb-4 d-flex flex-row flex-wrap">
          <div
            class="col-12 justify-content-start d-flex"
            style="overflow: hidden"
          >
            <div class="component-loading w-100 p-2 rounded mb-3"></div>
          </div>
          <div class="col-12 justify-content-start d-flex">
            <div class="component-loading w-50 p-2 rounded mb-4"></div>
          </div>
        </div>
      </div>
    </div>

    <div class="row" v-else-if="displayedQuizzes.length === 0">
      <div class="col-12 p-5 my-5 d-flex justify-content-center">
        <h4 style="color: grey">No result</h4>
      </div>
    </div>

    <div class="row" v-else>
      <div
        v-for="quiz in displayedQuizzes"
        :key="quiz._id"
        class="col-4"
        @click="quizSelectHandler(quiz)"
      >
        <div class="quiz shadow p-4 pb-3 mb-4 d-flex flex-row flex-wrap">
          <div
            class="col-12 justify-content-start d-flex"
            style="overflow: hidden"
          >
            <h4 style="font-weight: bold; white-space: nowrap">
              {{ quiz.title }}
            </h4>
          </div>
          <div class="col-12 justify-content-start d-flex">
            <h6 style="opacity: 0.5; font-size: 0.9em">
              {{ quiz.questions.length }} Questions
            </h6>
          </div>
          <div class="col-12 justify-content-end d-flex">
            <span
              class="badge"
              :style="[
                {
                  backgroundColor: getCategoryByName(quiz.category).background,
                  color: getCategoryByName(quiz.category).foreground,
                  fontSize: '14px',
                },
              ]"
              >{{ getCategoryByName(quiz.category).title }}</span
            >
          </div>
        </div>
      </div>
    </div>

    <transition name="fade">
      <div v-show="showResponseModal" class="modal-canvas">
        <div
          class="modal-box-large shadow d-flex flex-column justify-content-between"
          style="height: 80%"
        >
          <div class="row" style="height: 10%">
            <div class="col-9 p-2" style="overflow-x: hidden">
              <h3 style="color: white">
                Response History
                <span style="color: grey; font-size: 0.8em; margin-left: 8px">
                  {{ responseLoading ? "" : userResults.length }}
                </span>
              </h3>
            </div>
            <div class="col-3 d-flex justify-content-end">
              <div
                class="navbar-icon-container"
                @click="showResponseModal = false"
              >
                <span class="navbar-icon">
                  <b-icon icon="x" font-scale="1.5" />
                </span>
              </div>
            </div>
          </div>
          <div class="py-2 modal-scrollbar-container" style="height: 90%">
            <div v-if="responseLoading" class="col-12">
              <div
                v-for="(number, index) in 2"
                :key="index"
                class="col-12 d-flex pt-2 pb-4"
              >
                <div class="col-9">
                  <div
                    class="component-loading rounded p-2 mb-3"
                    style="width: 240px"
                  ></div>
                  <div
                    class="component-loading rounded p-2"
                    style="width: 100px"
                  ></div>
                </div>
                <div class="col-3 d-flex justify-content-end pe-3">
                  <div
                    class="component-loading rounded-circle"
                    style="height: 96px; width: 96px; padding: 0.8rem"
                  >
                    <div
                      class="h-100 w-100 rounded-circle"
                      style="background-color: var(--element-color)"
                    ></div>
                  </div>
                </div>
              </div>
            </div>
            <div
              v-else-if="userResults.length === 0"
              class="col-12 p-5 d-flex justify-content-center"
            >
              <h4 style="color: grey">No activity</h4>
            </div>
            <!-- Response History  -->
            <div
              v-else
              v-for="result in userResults"
              :key="result._id"
              class="col-12 d-flex"
            >
              <div class="col-12 d-flex pt-2 pb-4">
                <div class="col-9" style="overflow-x: hidden">
                  <div
                    style="font-size: 1.2em; color: white; font-weight: bold"
                  >
                    {{ result.quizName }}
                  </div>
                  <div style="font-size: 0.9em; color: grey">
                    {{ timeSince(result.createDateTime) }}
                  </div>
                  <div
                    class="badge mt-2"
                    :style="[
                      {
                        backgroundColor: $attrs.getCategoryByName(
                          result.category
                        ).background,
                        color: $attrs.getCategoryByName(result.category)
                          .foreground,
                        fontSize: '14px',
                      },
                    ]"
                  >
                    {{ $attrs.getCategoryByName(result.category).title }}
                  </div>
                </div>
                <div class="col-3 d-flex justify-content-end pe-3">
                  <progress-circle
                    :completed-steps="result.score"
                    :total-steps="result.fullScore"
                    :circle-color="'#262626'"
                    :start-color="
                      $attrs.getCategoryByName(result.category).background
                    "
                    :stop-color="
                      $attrs.getCategoryByName(result.category).background
                    "
                    :diameter="96"
                    :circle-width="8"
                  >
                    <span style="color: white; font-size: 1em"
                      >{{ result.score }} of {{ result.fullScore }}</span
                    >
                  </progress-circle>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-show="showProfileModal" class="modal-canvas">
        <div
          class="modal-box-small shadow d-flex flex-column pb-4"
          style="height: auto"
        >
          <div class="row">
            <div class="col-9 p-2" style="overflow-x: hidden">
              <h3 style="color: white">Profile</h3>
            </div>
            <div class="col-3 d-flex justify-content-end">
              <div
                class="navbar-icon-container"
                @click="showProfileModal = false"
              >
                <span class="navbar-icon">
                  <b-icon icon="x" font-scale="1.5" />
                </span>
              </div>
            </div>
          </div>
          <div class="row py-2">
            <div class="col-12 d-flex flex-row align-items-center">
              <div class="d-flex justify-content-center align-items-start">
                <img
                  :src="'https://api.multiavatar.com/' + user.name + '.svg'"
                  height="72"
                  alt
                />
              </div>
              <div class="ps-3 pe-5">
                <div
                  style="
                    color: white;
                    font-size: 1.2em;
                    margin-bottom: 4px;
                    font-weight: bold;
                  "
                >
                  {{ user.name }}
                </div>
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
import axios from "@/plugins/axios";
import { ProgressCircle } from "vue-progress-circle";
import SERVICE_URL from "../assets/service-url.json";

export default {
  name: "HomePage",
  components: { ProgressCircle },
  data() {
    return {
      user: {},
      quizzes: [],
      displayedQuizzes: [],
      categories: this.$attrs.categories,
      userResults: [],
      searchQuery: "",
      quizLabelTitle: "Explore",
      showResponseModal: false,
      showProfileModal: false,
      loading: false,
      quizLoading: true,
      responseLoading: true,
    };
  },
  methods: {
    async getUser() {
      if (localStorage.getItem("user") === null) {
        await axios.post(SERVICE_URL.USER_SERVICE + "/user").then((res) => {
          localStorage.setItem("user", JSON.stringify(res.data));
          this.user = res.data;
        });
      } else {
        this.user = JSON.parse(localStorage.getItem("user"));
      }
    },
    async fetchUserResults() {
      this.responseLoading = true;
      await axios
        .patch(SERVICE_URL.RESULT_SERVICE + "/result/user", {
          userId: this.user._id,
        })
        .then((res) => {
          this.userResults = res.data.resultList.reverse();
          this.responseLoading = false;
        });
    },
    async fetchQuizzes() {
      this.quizLoading = true;
      await axios.get(SERVICE_URL.QUIZ_SERVICE + "/quiz").then((res) => {
        this.quizzes = res.data.reverse();
        this.displayedQuizzes = this.quizzes;
        this.quizLoading = false;
      });
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
      return Math.floor(seconds) + " seconds ago";
    },
    clickCategoryHandler(category) {
      this.$router.push({
        name: "QuizCategory",
        params: {
          category: category,
          categoryName: category.name,
        },
      });
    },
    quizSelectHandler(quiz) {
      this.$router.push({
        name: "QuizDetail",
        params: { quiz: quiz, quizId: quiz._id },
      });
    },
    getCategoryByName(categoryName) {
      return this.$attrs.getCategoryByName(categoryName);
    },
    searchBoxKeyPressHandler(event) {
      if (event.key === "Enter") {
        this.searchHandler();
      }
    },
    async searchHandler() {
      this.quizLoading = true;
      if (this.searchQuery.trim() === "") {
        this.displayedQuizzes = this.quizzes;
        this.quizLabelTitle = "Explore";
        this.quizLoading = false;
        return;
      }

      this.quizLabelTitle =
        this.searchQuery === "" ? "Explore" : "Search Result";

      axios
        .patch(SERVICE_URL.EXTRACTOR_SERVICE + "/search", {
          query: this.searchQuery,
          quizzes: this.quizzes,
        })
        .then((res) => {
          this.displayedQuizzes = res.data.quizzes.reverse();
          this.quizLoading = false;
        });
    },
    setBodyScrollable(enabled) {
      document.body.style.paddingRight = enabled ? "0px" : "8px";
      document.body.style.overflow = enabled ? "initial" : "hidden";
    },
  },
  watch: {
    showProfileModal(newState, oldState) {
      this.setBodyScrollable(!newState);
    },
    showResponseModal(newState, oldState) {
      if (!oldState && newState) {
        this.fetchUserResults();
      }
      this.setBodyScrollable(!newState);
    },
  },
  async created() {
    await this.getUser();
    this.fetchQuizzes();
  },
};
</script>
