import { Subscription } from "expo-modules-core";
import type * as ExpoNotifications from "expo-notifications";
import {
  addNotificationReceivedListener as addExpoNotificationReceivedListener,
  addNotificationResponseReceivedListener as addExpoNotificationResponseReceivedListener,
} from "expo-notifications";
import {
  addAliyunNotificationReceivedListener,
  addAliyunNotificationResponseReceivedListener,
} from "./AliyunNotificationEmitter";
import type { Notification, NotificationResponse } from "./Notification.types";

export function addNotificationReceivedListener(
  listener: (event: Notification) => void
): Subscription {
  const expoSubscription = addExpoNotificationReceivedListener((event) => {
    if (isAliyunNotificationInExpoEvent(event)) {
      return;
    }
    listener(event);
  });
  const subscription = addAliyunNotificationReceivedListener(listener);

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
      if (isAliyunNotificationResponseInExpoEvent(event)) {
        return;
      }
      listener(event);
    }
  );
  const subscription = addAliyunNotificationResponseReceivedListener(listener);

  return {
    remove() {
      expoSubscription.remove();
      subscription.remove();
    },
  };
}

// TODO, should more exactly
function isAliyunNotificationInExpoEvent(
  notification: ExpoNotifications.Notification
) {
  return notification.request.trigger.type === "unknown";
}

function isAliyunNotificationResponseInExpoEvent(
  response: ExpoNotifications.NotificationResponse
) {
  return (
    isAliyunNotificationInExpoEvent(response.notification) ||
    // this is response by init Intent or new Intent of aliyun notification present by sdk
    (response.notification.request.trigger.type === "push" &&
      !response.notification.request.identifier &&
      !response.notification.date)
  );
}
