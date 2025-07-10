export default defineAppConfig({
  ui: {
    colors: {
      primary: "blue",
      neutral: "zinc",
    },
    button: {
      slots: {
        base: "cursor-pointer",
      },
    },
    navigationMenu: {
      slots: {
        link: "text-base",
        linkLeadingIcon: "text-lg",
      },
    },
  },
});
