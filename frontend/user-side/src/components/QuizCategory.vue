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
      </div>
    </div>

    <div class="row my-3">
      <div class="col d-flex align-items-start">
        <h1 class="text-light font-weight-bold">{{ category.title }}</h1>
      </div>
    </div>

    <div v-if="quizLoading" class="row">
      <div class="col-4" v-for="(number, index) in 6" :key="index">
        <div
          class="quiz shadow p-4 pb-3 mb-4 d-flex flex-row flex-wrap"
          style="cursor: initial"
        >
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

    <div v-else-if="displayedQuizzes.length === 0" class="row">
      <div class="col-12 p-5 my-5 d-flex justify-content-center">
        <h4 style="color: grey">No result</h4>
      </div>
    </div>

    <div v-else class="row">
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
  </div>
</template>

<script>
import axios from "axios";
import SERVICE_URL from "../assets/service-url.json";

export default {
  name: "QuizCategory",
  data() {
    return {
      filteredQuizzes: [],
      displayedQuizzes: [],
      categories: [],
      category: {},
      categoryName: "",
      searchQuery: "",
      quizLoading: true,
    };
  },
  methods: {
    async fetchQuizzesByCategory(categoryName) {
      this.quizLoading = true;
      var quizzes;

      await axios.get(SERVICE_URL.QUIZ_SERVICE + "/quiz").then((res) => {
        quizzes = res.data;
      });

      axios
        .patch(SERVICE_URL.EXTRACTOR_SERVICE + "/filter", {
          category: categoryName,
          quizzes: quizzes,
        })
        .then((res) => {
          this.filteredQuizzes = res.data.quizzes.reverse();
          this.displayedQuizzes = this.filteredQuizzes;
          this.quizLoading = false;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getCategoryByName(name) {
      var category = this.categories.find((category) => category.name === name);
      return category;
    },
    navigateToHomePage() {
      this.$router.push({ name: "HomePage" });
    },
    searchBoxKeyPressHandler(event) {
      if (event.key === "Enter") {
        this.searchHandler();
      }
    },
    async searchHandler() {
      this.quizLoading = true;
      if (this.searchQuery.trim() === "") {
        this.displayedQuizzes = this.filteredQuizzes;
        this.quizLoading = false;
        return;
      }
      axios
        .patch(SERVICE_URL.EXTRACTOR_SERVICE + "/search", {
          query: this.searchQuery,
          quizzes: this.filteredQuizzes,
        })
        .then((res) => {
          this.displayedQuizzes = res.data.quizzes.reverse();
          this.quizLoading = false;
        });
    },
    quizSelectHandler(quiz) {
      this.$router.push({
        name: "QuizDetail",
        params: { quiz: quiz, quizId: quiz._id },
      });
    },
  },
  async created() {
    this.categories = this.$attrs.categories;
    this.categoryName = this.$route.params.categoryName;
    this.category = this.$attrs.getCategoryByName(this.categoryName);
    this.fetchQuizzesByCategory(this.categoryName);
  },
};
</script>
