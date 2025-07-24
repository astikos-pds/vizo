import { defineStore } from "pinia";
import type { Department } from "~/types/domain";

export const useDepartmentStore = defineStore("department", () => {
  const currentDepartment = computed<Department | null>({
    get: () => {
      const item = localStorage.getItem("currentDepartment");
      if (!item) return null;
      return JSON.parse(item);
    },
    set: (value) => {
      localStorage.setItem("currentDepartment", JSON.stringify(value));
    },
  });

  const setCurrentDepartment = (value: Department | null) => {
    currentDepartment.value = value;
  };

  return {
    currentDepartment,
    setCurrentDepartment,
  };
});
