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
    card: {
      slots: {
        header: "p-3 sm:px-3",
        body: "p-3 sm:p-3",
        footer: "p-3 sm:px-3",
      },
    },
    navigationMenu: {
      slots: {
        link: "text-sm",
        linkLeadingIcon: "text-xl",
      },
    },
  },
});
