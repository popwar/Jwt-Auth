package org.lu;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyResponse<T>
{
	private T content;
}
