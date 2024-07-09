import {
  addNotificationReceivedListener as _addNotificationReceivedListener,
  addNotificationResponseReceivedListener as _addNotificationResponseReceivedListener,
  type Subscription,
} from "expo-notifications";
import type { Notification, NotificationResponse } from "./Notification.types";

export const addNotificationReceivedListener =
  _addNotificationReceivedListener as (
    listener: (event: Notification) => void
  ) => Subscription;

export const addNotificationResponseReceivedListener =
  _addNotificationResponseReceivedListener as (
    listener: (event: NotificationResponse) => void
  ) => Subscription;
