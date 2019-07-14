package com.waes.comparator.service;

import com.waes.comparator.controller.request.AspectEnum;
import com.waes.comparator.entity.Base64Entry;
import com.waes.comparator.repository.Base64DiffRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Random;


/**
 * Created by volkangumus on 14.07.2019
 */
public class Base64DiffServiceUnitTest {

    @InjectMocks
    private Base64DiffService diffService = new Base64DiffServiceImpl();

    @Mock
    private Base64DiffRepository diffRepository;

    private long randomId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.randomId = createRandomId();
    }

    @Test
    public void testSaveWithNotFound() {
        when(diffRepository.findById(randomId)).thenReturn(Optional.empty());

        diffService.save(randomId, "aGVsbG8gV2FlcyA=", AspectEnum.LEFT);

        Base64Entry expectedBase64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("aGVsbG8gV2FlcyA=");

        verify(diffRepository).findById(randomId);
        verify(diffRepository).save(expectedBase64Entry);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test
    public void testSaveWithLeftMissing() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setRight("right");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        diffService.save(randomId, "aGVsbG8gV2FlcyA=", AspectEnum.LEFT);

        Base64Entry expectedBase64Entry = base64Entry.setLeft("aGVsbG8gV2FlcyA=");

        verify(diffRepository).findById(randomId);
        verify(diffRepository).save(expectedBase64Entry);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test
    public void testSaveWithRightMissing() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("left");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        diffService.save(randomId, "aGVsbG8gV2FlcyA=", AspectEnum.RIGHT);

        Base64Entry expectedBase64Entry = base64Entry.setLeft("aGVsbG8gV2FlcyA=");

        verify(diffRepository).findById(randomId);
        verify(diffRepository).save(expectedBase64Entry);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSaveWithInvalidAspect() {
        when(diffRepository.findById(randomId)).thenReturn(Optional.empty());

        diffService.save(randomId, "aGVsbG8gV2FlcyA=", AspectEnum.MIDDLE);
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test
    public void testGetDiffWithNoEntryFound() {
        when(diffRepository.findById(randomId)).thenReturn(Optional.empty());

        String diff = diffService.getDiff(randomId);
        assertThat(diff, is("No entry found for the id: " + randomId));
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

   @Test
    public void testGetDiffWithMissingLeft() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setRight("aGVsbG8gV2FlcyA=");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        String diff = diffService.getDiff(randomId);
        assertThat(diff, is("Left side or Right side of the entry is blank"));
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

   @Test
    public void testGetDiffWithMissingRight() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("aGVsbG8gV2FlcyA=");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        String diff = diffService.getDiff(randomId);
        assertThat(diff, is("Left side or Right side of the entry is blank"));
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test
    public void testGetDiffWithEqual() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("aGVsbG8gV2FlcyA=")
                .setRight("aGVsbG8gV2FlcyA=");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        String diff = diffService.getDiff(randomId);
        assertThat(diff, is("Left and Right base64 datas are equal"));
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test
    public void testGetDiffWithDifferentLengths() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("aGVsbG8gV2FlcyA=")
                .setRight("V2FlcyA=");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        String diff = diffService.getDiff(randomId);
        assertThat(diff, is("Left and Right base64 datas do not have same size"));
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

    @Test
    public void testGetDiff() {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("SGVsbG8gV2Flcw==")
                .setRight("SGVsbG8gV0Flcw==");
        when(diffRepository.findById(randomId)).thenReturn(Optional.of(base64Entry));

        String diff = diffService.getDiff(randomId);
        assertThat(diff, is("Left and Right base64 datas are in same size and here is offset diff: 9"));
        verify(diffRepository).findById(randomId);
        verifyNoMoreInteractions(diffRepository);
    }

    private long createRandomId() {
        Random random = new Random();
        return random.nextLong();
    }
}