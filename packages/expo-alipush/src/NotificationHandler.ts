import { setNotificationHandler as _setNotificationHandler } from "expo-notifications";
import type { NotificationHandler } from "./NotificationHandler.types";

export const setNotificationHandler = _setNotificationHandler as (
  handler: NotificationHandler | null
) => void;
