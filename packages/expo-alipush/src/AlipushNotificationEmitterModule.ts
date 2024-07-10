import { Platform } from "expo-modules-core";

import { AlipushNotificationEmitterModule } from "./AlipushNotificationEmitterModule.types";

let warningHasBeenShown = false;

export default {
  addListener: () => {
    if (!warningHasBeenShown) {
      console.warn(
        `[expo-alipush] Emitting notifications is not yet fully supported on ${Platform.OS}. Adding a listener will have no effect.`
      );
      warningHasBeenShown = true;
    }
  },
  removeListeners: () => {},
} as AlipushNotificationEmitterModule;
