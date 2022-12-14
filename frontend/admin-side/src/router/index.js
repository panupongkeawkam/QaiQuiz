import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
  {
    path: "/login",
    name: "PasswordProtectedPage",
    component: () => import("../components/PasswordProtectedPage.vue"),
  },
  {
    path: "/",
    name: "HomePage",
    component: () => import("../components/HomePage.vue"),
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

var forceLogin = false;

router.beforeEach((to, from, next) => {
  var sessionExpire = localStorage.getItem("sessionExpire");
  var sessionExpireDate = new Date(sessionExpire);
  var currentDate = new Date();

  if (sessionExpireDate <= currentDate && !forceLogin) {
    forceLogin = true;
    next({ name: "PasswordProtectedPage" });
  }

  if (to.name === "PasswordProtectedPage" && sessionExpireDate > currentDate) {
    next({ name: "HomePage" });
  }

  next();
});

export default router;
