import {ConfigPlugin} from 'expo/config-plugins';

import type {AlipushConfig} from './shared';
import withAlipushAndroidConfig from './withAndroidConfig';
import withAlipushIOSConfig from './withIOSConfig';

export type * from './shared';

const withAlipushConfig: ConfigPlugin<AlipushConfig> = (
  config,
  pluginConfig,
) => {
  return [withAlipushAndroidConfig, withAlipushIOSConfig].reduce(
    (c, p) => p(c, pluginConfig),
    config,
  );
};

export default withAlipushConfig;
