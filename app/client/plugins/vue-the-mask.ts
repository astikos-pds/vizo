import type { Directive } from "vue";
import { mask } from "vue-the-mask";

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.directive("mask", mask as Directive);
});
