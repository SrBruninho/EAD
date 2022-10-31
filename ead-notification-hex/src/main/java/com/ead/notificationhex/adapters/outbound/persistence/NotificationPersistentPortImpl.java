package com.ead.notificationhex.adapters.outbound.persistence;

import com.ead.notificationhex.adapters.outbound.persistence.entities.NotificationEntity;
import com.ead.notificationhex.core.domain.NotificationDomain;
import com.ead.notificationhex.core.domain.PageInfo;
import com.ead.notificationhex.core.domain.enums.NotificationStatus;
import com.ead.notificationhex.core.ports.NotificationPersistencePort;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationPersistentPortImpl implements NotificationPersistencePort {

    private final NotificationJPARepository notificationJPARepository;
    private final ModelMapper modelMapper;

    public NotificationPersistentPortImpl(NotificationJPARepository notificationJPARepository, ModelMapper modelMapper) {
        this.notificationJPARepository = notificationJPARepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public NotificationDomain save(NotificationDomain notificationDomain) {
        NotificationEntity notificationEntity =
                notificationJPARepository.save( modelMapper.map( notificationDomain, NotificationEntity.class ) );
        return modelMapper.map( notificationEntity, NotificationDomain.class );
    }

    @Override
    public List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of( pageInfo.getPageNumber(), pageInfo.getPageSize() );
        return notificationJPARepository.findAllByUserIdAndNotificationStatus( userId, notificationStatus, pageable )
                .stream()
                .map( notificationEntity -> modelMapper.map( notificationEntity, NotificationDomain.class ) )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotificationDomain> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        Optional<NotificationEntity> notificationEntityOptional = notificationJPARepository.findByNotificationIdAndUserId( notificationId, userId );
        if ( notificationEntityOptional.isPresent() ) {
            return Optional.of( modelMapper.map( notificationEntityOptional.get(),NotificationDomain.class ) );
        }else {
            return Optional.empty();
        }

    }
}
