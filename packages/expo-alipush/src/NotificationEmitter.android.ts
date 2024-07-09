import { Subscription } from "expo-modules-core";
import type * as ExpoNotifications from "expo-notifications";
import {
  addNotificationReceivedListener as addExpoNotificationReceivedListener,
  addNotificationResponseReceivedListener as addExpoNotificationResponseReceivedListener,
} from "expo-notifications";
import {
  addAlipushNotificationReceivedListener,
  addAlipushNotificationResponseReceivedListener,
} from "./AlipushNotificationEmitter";
import type { Notification, NotificationResponse } from "./Notification.types";

export function addNotificationReceivedListener(
  listener: (event: Notification) => void
): Subscription {
  const expoSubscription = addExpoNotificationReceivedListener((event) => {
    if (isAlipushNotificationInExpoEvent(event)) {
      return;
    }
    listener(event);
  });
  const subscription = addAlipushNotificationReceivedListener(listener);

  return {
    remove() {
      expoSubscription.remove();
      subscription.remove();
    },
  };
}

export function addNotificationResponseReceivedListener(
  listener: (event: NotificationResponse) => void
): Subscription {
  const expoSubscription = addExpoNotificationResponseReceivedListener(
    (event) => {
      if (isAlipushNotificationResponseInExpoEvent(event)) {
        return;
      }
      listener(event);
    }
  );
  const subscription = addAlipushNotificationResponseReceivedListener(listener);

  return {
    remove() {
      expoSubscription.remove();
      subscription.remove();
    },
  };
}

// TODO, should more exactly
function isAlipushNotificationInExpoEvent(
  notification: ExpoNotifications.Notification
) {
  return notification.request.trigger.type === "unknown";
}

function isAlipushNotificationResponseInExpoEvent(
  response: ExpoNotifications.NotificationResponse
) {
  return (
    isAlipushNotificationInExpoEvent(response.notification) ||
    // this is response by init Intent or new Intent of aliyun notification present by sdk
    (response.notification.request.trigger.type === "push" &&
      !response.notification.request.identifier &&
      !response.notification.date)
  );
}
