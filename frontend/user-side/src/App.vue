<template>
  <div id="app">
    <router-view
      :key="$route.fullPath"
      :categories="categories"
      :getCategoryByName="getCategoryByName"
    />
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      quizzes: [],
      categories: [
        {
          foreground: "#880116",
          background: "#f57992",
          iconName: "gear",
          title: "General Knowledge",
          name: "general-knowledge"
        },
        {
          foreground: "#a4241b",
          background: "#f2976e",
          iconName: "music-note-beamed",
          title: "Film",
          name: "film"
        },
        {
          foreground: "#c68648",
          background: "#f4cc64",
          iconName: "controller",
          title: "Game",
          name: "game"
        },
        {
          foreground: "#4e8577",
          background: "#7be8b0",
          iconName: "bicycle",
          title: "Sport",
          name: "sport"
        },
        {
          foreground: "#005667",
          background: "#7ce1e8",
          iconName: "code-slash",
          title: "Computer",
          name: "computer"
        },
        {
          foreground: "#3155a9",
          background: "#61b9ed",
          iconName: "collection-play",
          title: "Anime & Manga",
          name: "anime-and-manga"
        },
        {
          foreground: "#2f3992",
          background: "#8394e9",
          iconName: "alt",
          title: "Math",
          name: "math"
        },
        {
          foreground: "#3f3173",
          background: "#a288ed",
          iconName: "bug",
          title: "Animal",
          name: "animal"
        },
        {
          foreground: "#82125b",
          background: "#f590d1",
          iconName: "globe",
          title: "History",
          name: "history"
        }
      ]
    };
  },
  methods: {
    getCategoryByName(categoryName) {
      var category = this.categories.find(
        category => category.name === categoryName
      );
      return category;
    }
  }
};
</script>

<style>
@import url("./assets/style.css");
</style>
