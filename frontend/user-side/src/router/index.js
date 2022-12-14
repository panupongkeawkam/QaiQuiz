import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "HomePage",
    component: () => import("../components/HomePage.vue"),
  },
  {
    path: "/category/:categoryName",
    name: "QuizCategory",
    component: () => import("../components/QuizCategory.vue"),
  },
  {
    path: "/quiz/:quizId",
    name: "QuizDetail",
    component: () => import("../components/QuizDetail.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  next();
});

export default router;
