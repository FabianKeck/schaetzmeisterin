package de.fabiankeck.schaetzmeisterinbackendserver.dao;

import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SmUserDao extends PagingAndSortingRepository <SmUser, String>{
}
