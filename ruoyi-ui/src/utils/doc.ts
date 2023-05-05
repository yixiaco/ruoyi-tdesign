/**
 * 修改title
 * @param title
 */
export function useTitle(title: string) {
  document.title = `${title} - ${import.meta.env.VITE_APP_TITLE}`;
}
