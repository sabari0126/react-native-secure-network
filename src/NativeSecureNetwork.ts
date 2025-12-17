import { TurboModuleRegistry, type TurboModule } from 'react-native';
import type { NetworkSecurityResult } from './utils/types';

export interface Spec extends TurboModule {
  getConnectionStatus(): Promise<NetworkSecurityResult>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('SecureNetwork');
