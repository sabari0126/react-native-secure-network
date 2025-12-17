#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkSecurityResult : NSObject

@property (nonatomic, assign) BOOL isSecureNetwork;
@property (nonatomic, strong) NSString *message;

- (instancetype)initWithIsSecureNetwork:(BOOL)isSecureNetwork message:(NSString *)message;

@end

@interface InsecureNetworkDetection : NSObject

+ (instancetype)shared;
- (nullable NetworkSecurityResult *)checkNetworkSecurity;

@end

NS_ASSUME_NONNULL_END
