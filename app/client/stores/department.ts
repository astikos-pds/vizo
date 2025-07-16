import { defineStore } from "pinia";
import type { Department, Municipality } from "~/types/domain";

export const useDepartmentStore = defineStore("municipality", () => {
  const currentDepartment = ref<Department>();

  const setCurrentDepartment = (value: Department) => {
    currentDepartment.value = value;
  };

  return {
    currentDepartment,
    setCurrentDepartment,
  };
});
