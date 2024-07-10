import {
  EventEmitter,
  type Subscription,
  UnavailabilityError,
} from "expo-modules-core";
import AlipushNotificationEmitterModule from "./AlipushNotificationEmitterModule";
import type {
  AlipushTriggeredNotification,
  AlipushTriggeredNotificationResponse,
} from "./AlipushNotificationEmitterModule.types";

const emitter = new EventEmitter(AlipushNotificationEmitterModule);

const didReceiveNotificationEventName = "onDidReceiveAlipushNotification";
const didReceiveNotificationResponseEventName =
  "onDidReceiveAlipushNotificationResponse";

export function addAlipushNotificationReceivedListener(
  listener: (event: AlipushTriggeredNotification) => void
): Subscription {
  return emitter.addListener(didReceiveNotificationEventName, listener);
}

export function addAlipushNotificationResponseReceivedListener(
  listener: (event: AlipushTriggeredNotificationResponse) => void
): Subscription {
  return emitter.addListener(didReceiveNotificationResponseEventName, listener);
}

export function getLastAlipushNotificationResponse() {
  if (!AlipushNotificationEmitterModule.getLastAlipushNotificationResponse) {
    throw new UnavailabilityError(
      "AlipushNotificationEmitter",
      "getLastAlipushNotificationResponse"
    );
  }
  return AlipushNotificationEmitterModule.getLastAlipushNotificationResponse();
}
