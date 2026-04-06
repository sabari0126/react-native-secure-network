#import "InsecureNetworkDetection.h"
#import <NetworkExtension/NetworkExtension.h>
#import <CoreLocation/CoreLocation.h>

@implementation NetworkSecurityResult

- (instancetype)initWithIsSecureNetwork:(BOOL)isSecureNetwork message:(NSString *)message code:(NSInteger)code {
    self = [super init];
    if (self) {
        _isSecureNetwork = isSecureNetwork;
        _message = message;
        _code = code;
    }
    return self;
}

@end

@implementation InsecureNetworkDetection

+ (instancetype)shared {
    static InsecureNetworkDetection *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        // Private initializer
    }
    return self;
}

- (nullable NetworkSecurityResult *)checkNetworkSecurity {
    // Check location permission first
    if (![self hasLocationPermission]) {
        return [[NetworkSecurityResult alloc] initWithIsSecureNetwork:NO
                                                              message:@"Location permission required for network security analysis."
                                                                code:9007];
    }

    // Check iOS version compatibility
    if (@available(iOS 14.0, *)) {
        return [self checkNetworkSecurityWithHotspotNetwork];
    } else {
        return [[NetworkSecurityResult alloc] initWithIsSecureNetwork:NO
                                                              message:@"iOS version does not support network security checks"
                                                                code:9008];
    }
}

- (nullable NetworkSecurityResult *)checkNetworkSecurityWithHotspotNetwork API_AVAILABLE(ios(14.0)) {
    __block NetworkSecurityResult *result = nil;
    dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);

    [NEHotspotNetwork fetchCurrentWithCompletionHandler:^(NEHotspotNetwork * _Nullable network) {
        if (!network) {
            result = [[NetworkSecurityResult alloc] initWithIsSecureNetwork:YES
                                                                    message:@"Wi-Fi information is unavailable"
                                                                      code:7003];
            dispatch_semaphore_signal(semaphore);
            return;
        }

        // Check for weak security types (iOS 15+ only)
        if (@available(iOS 15.0, *)) {
            NEHotspotNetworkSecurityType securityType = network.securityType;
            BOOL isSecure = [self isWeakSecurityType:securityType];
            NSString *message = isSecure ?
                @"Connected to a secure network. No additional concerns." :
                @"Connected to an open/unsecured Wi-Fi network. Please consider using a secure Wi-Fi network.";
            NSInteger code = isSecure ? 7001 : 9001;

            result = [[NetworkSecurityResult alloc] initWithIsSecureNetwork:isSecure
                                                                    message:message
                                                                      code:code];
            dispatch_semaphore_signal(semaphore);
            return;
        }

        result = nil;
        dispatch_semaphore_signal(semaphore);
    }];

    // Wait for network check with timeout (2 seconds)
    dispatch_semaphore_wait(semaphore, dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)));

    return result;
}

- (BOOL)hasLocationPermission {
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    return (status == kCLAuthorizationStatusAuthorizedWhenInUse ||
            status == kCLAuthorizationStatusAuthorizedAlways);
}

- (BOOL)isWeakSecurityType:(NEHotspotNetworkSecurityType)securityType API_AVAILABLE(ios(15.0)) {
    switch (securityType) {
        case NEHotspotNetworkSecurityTypeOpen:
            return NO;
        case NEHotspotNetworkSecurityTypeWEP:
            return NO;
        case NEHotspotNetworkSecurityTypePersonal:
            // WPA/WPA2/WPA3 using pre-shared secret - generally secure
            return YES;
        case NEHotspotNetworkSecurityTypeEnterprise:
            // WPA/WPA2/WPA3 using enterprise security - most secure
            return YES;
        case NEHotspotNetworkSecurityTypeUnknown:
            // Unknown security type could be risky
            return NO;
        default:
            return YES;
    }
}

@end
