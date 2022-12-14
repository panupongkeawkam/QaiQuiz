<template>
  <div class="screen-filled">
    <div
      class="container h-100 d-flex flex-wrap flex-column justify-content-center"
    >
      <div class="col-12 d-flex justify-content-center">
        <span class="text-light mb-4">
          <b-icon icon="lock" font-scale="3" style="margin-bottom: 1px" />
        </span>
      </div>
      <div class="col-12 d-flex flex-column align-items-center mb-5">
        <h1 class="mb-2" style="color: white">Management access blocked</h1>
        <h4 style="color: grey">To view, enter the password</h4>
      </div>
      <div class="col-12 d-flex flex-column align-items-center mb-5">
        <input
          type="password"
          class="text-input text-center"
          :class="{
            border: passwordIncorrect,
            'border-danger': passwordIncorrect,
          }"
          placeholder="Password"
          style="width: 320px; letter-spacing: 1px; font-size: 1.1em"
          v-model="password"
          @keypress="passwordKeypressHandler"
          @click="passwordIncorrect = false"
        />
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "PasswordProtectedPage",
  data() {
    return {
      password: "",
      passwordIncorrect: false,
    };
  },
  methods: {
    passwordKeypressHandler(event) {
      this.passwordIncorrect = false;

      if (event.key === "Enter" && this.password !== "") {
        this.passwordSubmittedHandler();
      }
    },
    passwordSubmittedHandler() {
      var correctPassword = this.$attrs.password;
      if (correctPassword !== this.password.trim()) {
        this.passwordIncorrect = true;
        this.password = "";
      } else if (correctPassword === this.password.trim()) {
        this.passwordIncorrect = false;
        this.password = "";

        var date = new Date();
        date.setMinutes(parseInt(date.getMinutes()) + 30);
        localStorage.setItem("sessionExpire", date);

        this.$router.push({ name: "HomePage" });
      }
    },
  },
};
</script>
