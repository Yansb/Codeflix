package com.yansb.admin.api.infrastructure.castMember;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberJpaEntity;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import com.yansb.admin.api.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public void deleteByID(final CastMemberID aMemberId) {
        final var anId = aMemberId.getValue();
        if (this.castMemberRepository.existsById(anId)) {
            this.castMemberRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<CastMember> findByID(final CastMemberID anID) {
        return this.castMemberRepository.findById(anID.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return save(aCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecificatio)
                .orElse(null);

        final var pageResult = this.castMemberRepository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CastMemberID> existsByIds(Iterable<CastMemberID> castMemberIDS) {
        final var ids = StreamSupport.stream(castMemberIDS.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();

        return this.castMemberRepository.existsByIds(ids).stream()
                .map(CastMemberID::from)
                .toList();
    }

    private CastMember save(CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember))
                .toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecificatio(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
