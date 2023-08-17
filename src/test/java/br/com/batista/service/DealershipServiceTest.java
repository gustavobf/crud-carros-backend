package br.com.batista.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.batista.dto.DealershipDTO;
import br.com.batista.model.Dealership;
import br.com.batista.repositories.DealershipRepository;

public class DealershipServiceTest {

	@Mock
	DealershipRepository repository;

	@InjectMocks
	DealershipService service;

	@Captor
	ArgumentCaptor<Dealership> dealershipCaptor;

	@Captor
	ArgumentCaptor<Long> dealershipIdCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAll() {
		final List<Dealership> dealershipList = new ArrayList<>();
		final Dealership newDealership = new Dealership(105l, "111", "name");
		dealershipList.add(newDealership);

		when(repository.findAll()).thenReturn(dealershipList);

		final List<DealershipDTO> list = service.getAll();
		final DealershipDTO dealershipDTO = list.get(0);
		assertEquals(newDealership.getId(), dealershipDTO.getId(), 0);
		assertEquals(newDealership.getCnpj(), dealershipDTO.getCnpj());
		assertEquals(newDealership.getName(), dealershipDTO.getNome());
	}

	@Test
	public void testGetById() {
		final Dealership newDealership = new Dealership(10l, "name", "country");
		when(repository.findById(newDealership.getId())).thenReturn(Optional.of(newDealership));

		final Optional<DealershipDTO> returnedDealership = service.getById(newDealership.getId());
		final DealershipDTO dealershipDTO = returnedDealership.get();
		assertEquals(newDealership.getId(), dealershipDTO.getId(), 0);
		assertEquals(newDealership.getCnpj(), dealershipDTO.getCnpj());
		assertEquals(newDealership.getName(), dealershipDTO.getNome());
	}

	@Test
	public void testCreate() {
		final DealershipDTO dealershipDTO = new DealershipDTO();
		dealershipDTO.setId(10l);
		dealershipDTO.setNome("name");
		dealershipDTO.setCnpj("123");

		when(repository.save(dealershipCaptor.capture())).thenReturn(service.convertToEntity(dealershipDTO));

		final DealershipDTO returnedDealership = service.create(dealershipDTO);
		assertEquals(dealershipDTO.getId(), dealershipDTO.getId(), 0);
		assertEquals(dealershipDTO.getCnpj(), returnedDealership.getCnpj());
		assertEquals(dealershipDTO.getNome(), returnedDealership.getNome());

		final Dealership dealershipSaved = dealershipCaptor.getValue();
		assertEquals(dealershipDTO.getId(), dealershipSaved.getId(), 0);
		assertEquals(dealershipDTO.getCnpj(), dealershipSaved.getCnpj());
		assertEquals(dealershipDTO.getNome(), dealershipSaved.getName());
	}

	@Test
	public void testDelete() {
		service.delete(15l);
		verify(repository, times(1)).deleteById(dealershipIdCaptor.capture());
		assertEquals(15, dealershipIdCaptor.getValue(), 0);
	}

	@Test
	public void testUpdate() {
		final DealershipDTO dealershipDTO = new DealershipDTO();
		dealershipDTO.setId(10l);
		dealershipDTO.setNome("name");
		dealershipDTO.setCnpj("123");

		when(repository.save(dealershipCaptor.capture())).thenReturn(service.convertToEntity(dealershipDTO));

		final DealershipDTO updatedDealership = service.update(dealershipDTO);

		assertEquals(dealershipDTO.getId(), updatedDealership.getId(), 0);
		assertEquals(dealershipDTO.getCnpj(), updatedDealership.getCnpj());
		assertEquals(dealershipDTO.getNome(), updatedDealership.getNome());

		final Dealership savedDealership = dealershipCaptor.getValue();
		assertEquals(dealershipDTO.getId(), savedDealership.getId(), 0);
		assertEquals(dealershipDTO.getCnpj(), savedDealership.getCnpj());
		assertEquals(dealershipDTO.getNome(), savedDealership.getName());
	}

}
