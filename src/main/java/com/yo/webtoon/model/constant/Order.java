package com.yo.webtoon.model.constant;

import com.yo.webtoon.model.dto.WebtoonIndexDto;
import com.yo.webtoon.repository.WebtoonRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
public enum Order {
    BEST {
        @Override
        public List<WebtoonIndexDto> search(WebtoonRepository webtoonRepository,
            Genre genre, boolean isComplete, Pageable pageable) {
            return webtoonRepository.findByGenreAndIsCompleteOrderByBest(
                genre, isComplete, pageable);
        }
    },
    REALTIME_BEST {
        @Override
        public List<WebtoonIndexDto> search(WebtoonRepository webtoonRepository,
            Genre genre, boolean isComplete, Pageable pageable) {
            return webtoonRepository.findByGenreAndIsCompleteOrderByRealTimeBest(
                genre, isComplete, pageable);
        }
    },
    WISHLIST {
        @Override
        public List<WebtoonIndexDto> search(WebtoonRepository webtoonRepository,
            Genre genre, boolean isComplete, Pageable pageable) {
            return webtoonRepository.findByGenreAndIsCompleteOrderByWishlist(
                genre, isComplete, pageable);
        }
    },
    UP_TO_DATE {
        @Override
        public List<WebtoonIndexDto> search(WebtoonRepository webtoonRepository,
            Genre genre, boolean isComplete, Pageable pageable) {
            return webtoonRepository.findByGenreAndIsCompleteOrderByUpToDate(
                genre, isComplete, pageable);
        }
    };


    public abstract List<WebtoonIndexDto> search(WebtoonRepository webtoonRepository,
        Genre genre, boolean isComplete, Pageable pageable);
}
