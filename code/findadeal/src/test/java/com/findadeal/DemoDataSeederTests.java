package com.findadeal;

import com.findadeal.listing.ListingRepository;
import com.findadeal.user.UserRepository;
import com.findadeal.watchlist.WatchlistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles({"test", "demo"})
@SpringBootTest
class DemoDataSeederTests {

    @Autowired ListingRepository listingRepository;
    @Autowired WatchlistRepository watchlistRepository;
    @Autowired UserRepository userRepository;

    @Test
    void seedsListingsAndWatchlistsForDemoUser() {
        var demoUser = userRepository.findByUsernameIgnoreCase("demo");
        assertTrue(demoUser.isPresent());

        assertEquals(14, listingRepository.count());

        var watchlists = watchlistRepository
                .findAllByUserId(demoUser.get().getId(), Pageable.unpaged())
                .getContent();

        assertEquals(2, watchlists.size());
        List<String> keywords = watchlists.stream().map(w -> w.getKeyword()).toList();
        assertTrue(keywords.contains("iphone 13"));
        assertTrue(keywords.contains("rtx 4070 super"));
    }
}
