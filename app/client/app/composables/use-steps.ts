export const useSteps = () => {
  const initialStep = 0;

  const current = useState("current_step", () => 0);
  const totalSteps = useState("total_steps", () => 0);

  const hasNext = computed(() => current.value < totalSteps.value - 1);
  const hasPrev = computed(() => current.value > initialStep);

  const next = () => {
    if (hasNext) current.value++;
  };

  const prev = () => {
    if (hasPrev) current.value--;
  };

  const setTotalSteps = (newTotalSteps: number) =>
    (totalSteps.value = newTotalSteps);

  const setStep = (step: number) => {
    if (step >= 0 && step < totalSteps.value) current.value = step;
  };

  const goToStart = () => {
    setStep(0);
  };

  return {
    current,
    hasNext,
    hasPrev,
    next,
    prev,
    setTotalSteps,
    setStep,
    goToStart,
  };
};
