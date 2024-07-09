import { requireNativeModule } from "expo-modules-core";

import { AlipushNotificationEmitterModule } from "./AlipushNotificationEmitterModule.types";

export default requireNativeModule<AlipushNotificationEmitterModule>(
  "AlipushNotificationEmitter"
);
