import { defineStore } from "pinia";
import type { Department } from "~/types/domain";

export const useDepartmentStore = defineStore("municipality", () => {
  const currentDepartment = computed<Department | undefined>({
    get: () => JSON.parse(localStorage.getItem("current_department") ?? ""),
    set: (value: Department | undefined) =>
      localStorage.setItem("current_department", JSON.stringify(value ?? {})),
  });

  const setCurrentDepartment = (value: Department | undefined) => {
    currentDepartment.value = value;
  };

  return {
    currentDepartment,
    setCurrentDepartment,
  };
});
