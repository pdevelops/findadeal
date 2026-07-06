package com.findadeal.common.seed;

import com.findadeal.listing.Listing;
import com.findadeal.listing.ListingRepository;
import com.findadeal.user.User;
import com.findadeal.user.UserRepository;
import com.findadeal.watchlist.Watchlist;
import com.findadeal.watchlist.WatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@Profile("demo")
@Order(2)
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);
    private static final String SEED_SOURCE = "demo-seed";

    private final ListingRepository listingRepository;
    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;

    public DemoDataSeeder(
            ListingRepository listingRepository,
            WatchlistRepository watchlistRepository,
            UserRepository userRepository
    ) {
        this.listingRepository = listingRepository;
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (!listingRepository.findBySourceAndExternalIdIn(SEED_SOURCE, List.of("iphone-13-0")).isEmpty()) {
            log.info("demo.seed alreadySeeded");
            return;
        }

        User demo = userRepository.findByUsernameIgnoreCase("demo")
                .orElseThrow(() -> new IllegalStateException(
                        "demo user not found; DemoUserSeeder must run before DemoDataSeeder"));

        seedListing("iphone-13-0", "iPhone 13 128GB - mint condition, box included", 1050);
        seedListing("iphone-13-1", "iPhone 13 256GB - barely used", 980);
        seedListing("iphone-13-2", "iPhone 13 128GB - good condition", 940);
        seedListing("iphone-13-3", "iPhone 13 Pro 128GB - excellent condition", 900);
        seedListing("iphone-13-4", "iPhone 13 128GB - some wear, fully functional", 870);
        seedListing("iphone-13-5", "iPhone 13 128GB - quick sale, must go this weekend", 610);
        seedListing("iphone-13-6", "iPhone 13 Mini 128GB - priced to sell fast", 590);

        seedListing("rtx-4070-super-0", "RTX 4070 Super - mint condition, box included", 1120);
        seedListing("rtx-4070-super-1", "RTX 4070 Super - barely used", 1080);
        seedListing("rtx-4070-super-2", "RTX 4070 Super - good condition", 1010);
        seedListing("rtx-4070-super-3", "RTX 4070 Super - excellent condition", 970);
        seedListing("rtx-4070-super-4", "RTX 4070 Super - some wear, fully functional", 940);
        seedListing("rtx-4070-super-5", "RTX 4070 Super - quick sale, must go this weekend", 620);
        seedListing("rtx-4070-super-6", "RTX 4070 Super - priced to sell fast", 600);

        createWatchlist(demo, "iphone 13", 20);
        createWatchlist(demo, "rtx 4070 super", 20);

        log.info("demo.seed complete listings=14 watchlists=2");
    }

    private void seedListing(String externalId, String title, int price) {
        Listing listing = new Listing();
        listing.setSource(SEED_SOURCE);
        listing.setExternalId(externalId);
        listing.setTitle(title);
        listing.setPrice(BigDecimal.valueOf(price));
        listing.setCurrency("AUD");
        listing.setLocation("Sydney NSW");
        listing.setSourceCategory("Electronics");
        listing.setLastSeen(OffsetDateTime.now());
        listingRepository.save(listing);
    }

    private void createWatchlist(User user, String keyword, int percentageThreshold) {
        watchlistRepository.save(new Watchlist(user, keyword, percentageThreshold));
    }
}
