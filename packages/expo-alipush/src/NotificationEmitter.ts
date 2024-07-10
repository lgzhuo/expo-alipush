import {
  addNotificationReceivedListener as _addNotificationReceivedListener,
  addNotificationResponseReceivedListener as _addNotificationResponseReceivedListener,
  getLastNotificationResponseAsync as _getLastNotificationResponseAsync,
  type Subscription,
} from "expo-notifications";
import type { Notification, NotificationResponse } from "./Notification.types";

export const addNotificationReceivedListener: (
  listener: (event: Notification) => void
) => Subscription = _addNotificationReceivedListener;

export const addNotificationResponseReceivedListener: (
  listener: (event: NotificationResponse) => void
) => Subscription = _addNotificationResponseReceivedListener;

export const getLastNotificationResponseAsync: () => Promise<NotificationResponse | null> =
  _getLastNotificationResponseAsync;
