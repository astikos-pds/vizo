import { defineStore } from "pinia";

export const useToastStore = defineStore("toast", () => {
  const toastId = ref<string | number>("");
  const status = ref<number>(0);

  const setToastId = (value: string | number) => {
    toastId.value = value;
  };

  const setStatus = (value: number) => {
    status.value = value;
  };

  return {
    toastId,
    setToastId,
    status,
    setStatus,
  };
});
