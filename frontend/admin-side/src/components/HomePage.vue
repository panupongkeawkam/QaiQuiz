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
        <div class="navbar-icon-container" @click="showSignOutDialog = true">
          <span class="navbar-icon">
            <b-icon icon="box-arrow-left" style="margin-right: 3px; margin-bottom: 1px" />
          </span>
        </div>
      </div>
    </div>

    <div class="row my-3">
      <div class="col-8 d-flex align-items-center">
        <h1 class="text-light font-weight-bold">Quizzes</h1>
        <h3 class="ms-4 mt-1" style="color: grey">{{ quizLoading ? "" : displayedQuizzes.length }}</h3>
      </div>
      <div class="col-4 d-flex align-items-start justify-content-end">
        <div class="primary-button shadow bg-light text-dark" @click="showCreateQuizModal = true">
          <b-icon icon="plus" font-scale="1.5" style="font-size: 0.9em" />
          <span class="me-1">Quiz</span>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-12 py-4 d-flex flex-row horizontal-scroll">
        <div
          v-for="(category, index) in categories"
          :key="category.name"
          class="filter-button"
          :style="[
            {
              backgroundColor:
                selectedCategoryIndex === index
                  ? `${categories[selectedCategoryIndex].background}44`
                  : 'rgb(0, 0, 0, 0%)',
              borderColor:
                selectedCategoryIndex === index
                  ? categories[selectedCategoryIndex].background
                  : 'lightgrey',
              color:
                selectedCategoryIndex === index
                  ? categories[selectedCategoryIndex].background
                  : 'lightgrey',
            },
          ]"
          @click="selectCategoryFilterHandler(index)"
        >{{ category.title }}</div>
      </div>
    </div>

    <div v-if="quizLoading" class="row">
      <div class="col-4" v-for="(number, index) in 9" :key="index">
        <div class="quiz shadow p-4 pb-3 mb-4 d-flex flex-row flex-wrap">
          <div class="col-12 justify-content-start d-flex" style="overflow: hidden">
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
        <h4 style="color: grey">No quiz</h4>
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
          <div class="col-12 justify-content-start d-flex" style="overflow: hidden">
            <h4 style="font-weight: bold; white-space: nowrap">{{ quiz.title }}</h4>
            <!-- <div class="component-loading mb-2" style="width: 200px; height: 12px; border-radius: 8px"></div> -->
          </div>
          <div class="col-12 justify-content-start d-flex">
            <h6 style="opacity: 0.5; font-size: 0.9em">{{ quiz.questions.length }} Questions</h6>
            <!-- <div class="component-loading mb-2" style="width: 60px; height: 12px; border-radius: 8px"></div> -->
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
            >{{ getCategoryByName(quiz.category).title }}</span>
          </div>
        </div>
      </div>
    </div>

    <transition name="fade">
      <div v-show="showSignOutDialog" class="modal-canvas">
        <div class="modal-box-small shadow d-flex flex-column" style="height: auto">
          <div class="row mb-4">
            <div class="col-12 p-2" style="overflow-x: hidden">
              <h3 style="color: white">Are you sure to sign out?</h3>
            </div>
          </div>
          <div class="row">
            <div class="col-12 d-flex justify-content-end">
              <div
                class="primary-button"
                style="background-color: transparent"
                @click="showSignOutDialog = false"
              >Cancel</div>
              <div
                class="primary-button shadow"
                style="background-color: tomato"
                @click="signOutHandler()"
              >Sign Out</div>
            </div>
          </div>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-show="showCreateQuizModal" class="modal-canvas">
        <div class="modal-box-large shadow d-flex flex-column" style="height: auto">
          <div class="row">
            <div class="col-9 p-2" style="overflow-x: hidden">
              <h3 style="color: white">Create Quiz</h3>
            </div>
            <div class="col-3 d-flex justify-content-end">
              <div class="navbar-icon-container" @click="closeCreateQuizModal()">
                <span class="navbar-icon">
                  <b-icon icon="x" font-scale="1.5" />
                </span>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12 py-3 px-3 mb-5">
              <input
                type="text"
                placeholder="Quiz title"
                v-model="newQuizTitle"
                maxlength="60"
                class="text-input-underline fs-5 w-100"
              />
            </div>
            <div class="col-12 d-flex flex-wrap mb-3">
              <div
                v-for="category in categories"
                :key="category.name"
                class="filter-button"
                @click="
                  newQuizCategoryName =
                    newQuizCategoryName === category.name ? '' : category.name
                "
                :style="[
                  {
                    backgroundColor:
                      newQuizCategoryName === category.name
                        ? `${category.background}44`
                        : 'rgb(0, 0, 0, 0%)',
                    borderColor:
                      newQuizCategoryName === category.name
                        ? category.background
                        : 'lightgrey',
                    color:
                      newQuizCategoryName === category.name
                        ? category.background
                        : 'lightgrey',
                  },
                ]"
              >{{ category.title }}</div>
            </div>
          </div>
          <div class="row">
            <div class="col-12">
              <div class="col-6 d-flex align-items-center">
                <h6 v-show="isInvalid" class style="color: tomato;">{{ invalidMessage }}</h6>
              </div>
            </div>
            <div class="col-12 d-flex justify-content-end">
              <div
                class="primary-button"
                style="background-color: transparent"
                @click="closeCreateQuizModal()"
              >Cancel</div>
              <div
                class="primary-button shadow"
                style="background-color: white; color: black"
                @click="createQuizHandler()"
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
  name: "HomePage",
  data() {
    return {
      showSignOutDialog: false,
      showCreateQuizModal: false,
      newQuizTitle: "",
      searchQuery: "",
      categories: this.$attrs.categories,
      selectedCategoryIndex: -1,
      quizzes: [],
      filteredQuizzes: [],
      displayedQuizzes: [],
      newQuizCategoryName: "",
      isInvalid: false,
      invalidMessage: "",
      quizLoading: true,
      loading: false,
    };
  },
  methods: {
    signOutHandler() {
      localStorage.setItem("sessionExpire", new Date());
      this.$router.push({ name: "PasswordProtectedPage" });
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

      await axios
        .patch(SERVICE_URL.EXTRACTOR_SERVICE + "/search", {
          query: this.searchQuery.trim(),
          quizzes: this.filteredQuizzes,
        })
        .then((res) => {
          this.displayedQuizzes = res.data.quizzes;
          this.quizLoading = false;
        });
    },
    async selectCategoryFilterHandler(index) {
      this.quizLoading = true;

      if (this.selectedCategoryIndex === index) {
        this.selectedCategoryIndex = -1;
        this.filteredQuizzes = this.quizzes;
        this.displayedQuizzes = this.quizzes;
        this.quizLoading = false;
        return;
      }

      this.selectedCategoryIndex = index;
      await axios
        .patch(SERVICE_URL.EXTRACTOR_SERVICE + "/filter", {
          category: this.categories[index].name,
          quizzes: this.quizzes,
        })
        .then((res) => {
          this.filteredQuizzes = res.data.quizzes;
          this.displayedQuizzes = this.filteredQuizzes;
          this.quizLoading = false;
        });
    },
    getCategoryByName(categoryName) {
      return this.$attrs.getCategoryByName(categoryName);
    },
    quizSelectHandler(quiz) {
      this.$router.push({
        name: "QuizDetail",
        params: { quiz: quiz, quizId: quiz._id },
      });
    },
    async createQuizHandler() {
      this.isInvalid = false;
      this.invalidMessage = "";

      if (this.newQuizTitle.trim() === "") {
        this.isInvalid = true;
        this.invalidMessage = "Invalid: Quiz title cannot be empty";
        return;
      }

      if (this.newQuizCategoryName === "") {
        this.isInvalid = true;
        this.invalidMessage = "Invalid: Please choose quiz category";
        return;
      }

      this.loading = true;
      await axios
        .post(SERVICE_URL.QUIZ_SERVICE + "/quiz", {
          title: this.newQuizTitle.trim(),
          category: this.newQuizCategoryName,
        })
        .then((res) => {
          this.quizzes.unshift(res.data);
          this.filteredQuizzes = this.quizzes;
          this.displayedQuizzes = this.quizzes;
          this.loading = false;
          this.closeCreateQuizModal();
        })
        .catch((err) => {
          this.loading = false;
          this.isInvalid = true;
          this.invalidMessage = "Invalid: Quiz title is used";
        });
    },
    async getQuizzes() {
      this.quizLoading = true;

      await axios
        .get(SERVICE_URL.QUIZ_SERVICE + "/quiz?role=admin")
        .then((res) => {
          this.quizzes = res.data.reverse();
          this.filteredQuizzes = this.quizzes;
          this.displayedQuizzes = this.quizzes;
          this.quizLoading = false;
        });
    },
    closeCreateQuizModal() {
      this.newQuizTitle = "";
      this.newQuizCategoryName = "";
      this.isInvalid = false;
      this.invalidMessage = "";
      this.showCreateQuizModal = false;
    },
  },
  async created() {
    await this.getQuizzes();
  },
};
</script>
